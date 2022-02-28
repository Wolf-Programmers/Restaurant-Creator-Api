package com.example.restaurantapi.service;

import com.example.restaurantapi.biznesobject.OpeningTimes;
import com.example.restaurantapi.dto.restaurant.AddRestaurantDto;
import com.example.restaurantapi.dto.restaurant.CreatedRestaurantDto;
import com.example.restaurantapi.model.OpeningPeriod;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.User;
import com.example.restaurantapi.repository.OpeningPeriodRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import com.example.restaurantapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author Szymon Królik
 */
@Service
@AllArgsConstructor
public class RestaurantService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final OpeningPeriodRepository openingPeriodRepository;
    private final ValidationService validationService;
    private final EmailService emailService;
    private List<String> validationResult = new ArrayList<>();

    /*
        Add restaurant to user account
     */
    public ServiceReturn addRestaurantToUserAccount(AddRestaurantDto addRestaurantDto) throws ParseException {
        List<OpeningTimes> openingTimes = new ArrayList<>();
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

        //Prepare restaurant dto for save
        Restaurant restaurant = Restaurant.of(addRestaurantDto);
        restaurant.setUser_id(optionalUser.get());
        Restaurant createdRestaurant = restaurantRepository.save(restaurant);

        openingTimes = addRestaurantDto.getOpeningTimes();

        for (int i = 0; i < openingTimes.size(); i++) {
            OpeningPeriod openingPeriod = OpeningPeriod.of(openingTimes.get(i));
            openingPeriod.setRestaurant(createdRestaurant);

            OpeningPeriod periodRet = openingPeriodRepository.save(openingPeriod);

        }


        CreatedRestaurantDto restaurantDto = CreatedRestaurantDto.of(createdRestaurant);
        restaurantDto.setOpeningTimes(openingTimes);

        ret.setValue(restaurantDto);
        ret.setStatus(1);


        return ret;


    }

}
