package com.example.restaurantapi.service;

import com.example.restaurantapi.biznesobject.MenuInformation;
import com.example.restaurantapi.biznesobject.OpeningTimes;
import com.example.restaurantapi.biznesobject.RestaurantTypes;
import com.example.restaurantapi.dto.restaurant.AddEmployeeDto;
import com.example.restaurantapi.dto.restaurant.AddRestaurantDto;
import com.example.restaurantapi.dto.restaurant.CreatedRestaurantDto;
import com.example.restaurantapi.dto.restaurant.InfoRestaurantDto;
import com.example.restaurantapi.model.*;
import com.example.restaurantapi.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author Szymon Królik
 */
@Service
@AllArgsConstructor
public class RestaurantService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final OpeningPeriodRepository openingPeriodRepository;
    private final RestaurantTypeRepository restaurantTypeRepository;
    private final ItemRepository itemRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidationService validationService;
    private final EmailService emailService;
    private List<String> validationResult = new ArrayList<>();

    /**
     * Function for saving new restaurnt in database
     * @author Szymon Królik
     * @param addRestaurantDto
     * @return CreatedRestaurantDto
     * @throws ParseException
     */

    public ServiceReturn addRestaurantToUserAccount(AddRestaurantDto addRestaurantDto) throws ParseException {
        List<OpeningTimes> openingTimes = new ArrayList<>();
        List<RestaurantType> restaurantTypes = new ArrayList<>();
        List<RestaurantTypes> createdResTypes = new ArrayList<>();
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();


        Optional<User> optionalUser = userRepository.findById(addRestaurantDto.getOwner());
        if (optionalUser.isEmpty()) {
            ret.setStatus(-1);
            ret.setMessage("Nie znaleziono takiego użytkownika");

            return ret;
        }

        validationResult = validationService.restaurantValidation(addRestaurantDto);
        if (validationResult.size() > 0) {
            ret.setStatus(-1);
            ret.setErrorList(validationResult);
            ret.setValue(addRestaurantDto);

            return ret;
        }

        //Prepare restaurant types
        for (int i = 0; i < addRestaurantDto.getRestaurantTypesList().size(); i++) {
            RestaurantType restaurantType = new RestaurantType();
            Long typeId = Long.valueOf(addRestaurantDto.getRestaurantTypesList().get(i).getId());
            Optional<RestaurantType> restaurantTypeOptional = restaurantTypeRepository.findById(typeId);
            if (restaurantTypeOptional.isEmpty()) {
                ret.setStatus(0);
                ret.setMessage("Nie znaleziono takiego typu restauracji");
                return  ret;
            }
            restaurantTypes.add(restaurantTypeOptional.get());
        }

        //Prepare restaurant dto for save
        Restaurant restaurant = Restaurant.of(addRestaurantDto);
        restaurant.setUser_id(optionalUser.get());
        restaurant.setRestaurantTypes(restaurantTypes);

        Restaurant createdRestaurant = restaurantRepository.save(restaurant);

        openingTimes = addRestaurantDto.getOpeningTimes();

        //Add opening hours to restaurant
        for (int i = 0; i < openingTimes.size(); i++) {
            OpeningPeriod openingPeriod = OpeningPeriod.of(openingTimes.get(i));
            openingPeriod.setRestaurant(createdRestaurant);

            OpeningPeriod periodRet = openingPeriodRepository.save(openingPeriod);

        }
        CreatedRestaurantDto restaurantDto = CreatedRestaurantDto.of(createdRestaurant);
        restaurantDto.setOpeningTimes(openingTimes);

        //Get restaurant types to return
        List<RestaurantType> getCreatedRestaurantType = createdRestaurant.getRestaurantTypes();
        for (RestaurantType restaurantType : getCreatedRestaurantType) {
            RestaurantTypes type = RestaurantTypes.of(restaurantType);
            createdResTypes.add(type);
        }
        //Set restaurant types
        restaurantDto.setRestaurantTypes(createdResTypes);

        ret.setValue(restaurantDto);
        ret.setStatus(1);


        return ret;
    }


    /**
     * Find restaurant by specific id
     * @author Szymon Królik
     * @param id
     * @return InfoRestaurantDto
     */
    public ServiceReturn getRestaurant(Long id) {
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();
        List<RestaurantTypes> restaurantTypesList = new ArrayList<>();
        List<OpeningTimes> openingTimesList = new ArrayList<>();
        List<MenuInformation> menuInformationList =  new ArrayList<>();

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (optionalRestaurant.isEmpty()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiej restauracji");
            return ret;
        }

        InfoRestaurantDto infoRestaurantDto = InfoRestaurantDto.of(optionalRestaurant.get());
        //get menu list
        List<Menu> menus = optionalRestaurant.get().getMenus();
        for (Menu menu : menus) {
            MenuInformation menuInformation = MenuInformation.of(menu);
            menuInformationList.add(menuInformation);
        }
        infoRestaurantDto.setMenus(menuInformationList);

        //Get opening hours
        List<OpeningPeriod> openingPeriodList = openingPeriodRepository.findByRestaurant(optionalRestaurant.get());
        if (openingPeriodList.size() > 0) {
            for (OpeningPeriod openingPeriods : openingPeriodList) {
                OpeningTimes openingTimes = OpeningTimes.of(openingPeriods);
                openingTimesList.add(openingTimes);

            }
            infoRestaurantDto.setOpeningPeriod(openingTimesList);
        }

        //Get restaurant types
        List<RestaurantType> getRestaurantTypeList = optionalRestaurant.get().getRestaurantTypes();
        for (RestaurantType type : getRestaurantTypeList) {
            RestaurantTypes restaurantTypes = RestaurantTypes.of(type);
            restaurantTypesList.add(restaurantTypes);
        }


        infoRestaurantDto.setRestaurantTypes(restaurantTypesList);

        ret.setStatus(1);
        ret.setValue(infoRestaurantDto);
        return ret;

    }


    /**
     * Find restaurants by name
     * @author Szymon Królik
     * @param name
     * @return List<InfoRestaurantDto>
     */
    public ServiceReturn getRestaurantByName(String name) {
        ServiceReturn ret = new ServiceReturn();
        List<InfoRestaurantDto> infoRestaurantDtoList = new ArrayList<>();


        List<Restaurant> optionalRestaurant = restaurantRepository.findByNameContaining(name);
        if (optionalRestaurant.size() == 0) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiej restauracji");
            return ret;
        }
       for (Restaurant res : optionalRestaurant) {
           InfoRestaurantDto dto = InfoRestaurantDto.of(res);
           List<MenuInformation> menuInformationList = new ArrayList<>();
           List<OpeningTimes> openingTimesList = new ArrayList<>();
           List<RestaurantTypes> restaurantTypesList = new ArrayList<>();
           //Get menu list
           List<Menu> menus = res.getMenus();
           for (Menu menu: menus) {
               MenuInformation menuInformation = MenuInformation.of(menu);
               menuInformationList.add(menuInformation);
           }
           dto.setMenus(menuInformationList);

           List<OpeningPeriod> openingPeriodList = openingPeriodRepository.findByRestaurant(res);
           if (openingPeriodList.size() > 0) {
               for (OpeningPeriod openingPeriod : openingPeriodList) {
                   OpeningTimes openingTimes = OpeningTimes.of(openingPeriod);
                   openingTimesList.add(openingTimes);
               }
               dto.setOpeningPeriod(openingTimesList);
           }

           List<RestaurantType> getRestaurantTypeList = res.getRestaurantTypes();
           for (RestaurantType type : getRestaurantTypeList) {
               RestaurantTypes restaurantTypes = RestaurantTypes.of(type);
               restaurantTypesList.add(restaurantTypes);
           }
           dto.setRestaurantTypes(restaurantTypesList);

           infoRestaurantDtoList.add(dto);
       }

        ret.setStatus(1);
        ret.setValue(infoRestaurantDtoList);
        return ret;

    }

    /**
     * Find restaurant by city
     * @author Szymon Królik
     * @param city
     * @return List<InfoRestaurantDto>
     */
    public ServiceReturn getRestaurantsByCity(String city) {
        ServiceReturn ret = new ServiceReturn();
        List<InfoRestaurantDto> infoRestaurantDtoList = new ArrayList<>();

        List<Restaurant> optionalRestaurant = restaurantRepository.findByCity(city);
        if (optionalRestaurant.size() == 0) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono restauracji w podanym mieście");
            return ret;
        }
        for (Restaurant res : optionalRestaurant) {
            InfoRestaurantDto dto = InfoRestaurantDto.of(res);
            List<MenuInformation> menuInformationList = new ArrayList<>();
            List<OpeningTimes> openingTimesList = new ArrayList<>();
            List<RestaurantTypes> restaurantTypesList = new ArrayList<>();
            //Get menu list
            List<Menu> menus = res.getMenus();
            for (Menu menu: menus) {
                MenuInformation menuInformation = MenuInformation.of(menu);
                menuInformationList.add(menuInformation);
            }
            dto.setMenus(menuInformationList);

            List<OpeningPeriod> openingPeriodList = openingPeriodRepository.findByRestaurant(res);
            if (openingPeriodList.size() > 0) {
                for (OpeningPeriod openingPeriod : openingPeriodList) {
                    OpeningTimes openingTimes = OpeningTimes.of(openingPeriod);
                    openingTimesList.add(openingTimes);
                }
                dto.setOpeningPeriod(openingTimesList);
            }

            List<RestaurantType> getRestaurantTypeList = res.getRestaurantTypes();
            for (RestaurantType type : getRestaurantTypeList) {
                RestaurantTypes restaurantTypes = RestaurantTypes.of(type);
                restaurantTypesList.add(restaurantTypes);
            }
            dto.setRestaurantTypes(restaurantTypesList);

            infoRestaurantDtoList.add(dto);
        }

        ret.setStatus(1);
        ret.setValue(infoRestaurantDtoList);
        return ret;

    }

}
