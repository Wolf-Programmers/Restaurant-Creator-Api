package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.menu.CreateMenuDto;
import com.example.restaurantapi.dto.menu.CreatedMenuDto;
import com.example.restaurantapi.model.Menu;
import com.example.restaurantapi.model.MenuType;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.User;
import com.example.restaurantapi.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author Szymon Królik
 */
@Service
@AllArgsConstructor
public class MenuService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final MenuTypeRepository menuTypeRepository;
    private final ItemRepository itemRepository;
    private final ItemTypeRepository itemTypeRepository;
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private List<String> validationResult = new ArrayList<>();

    public ServiceReturn createMenu(CreateMenuDto createMenuDto) {
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();

        validationResult = validationService.menuValidation(createMenuDto);
        if (validationResult.size() > 0) {
            ret.setStatus(0);
            ret.setErrorList(validationResult);
            ret.setMessage("Proszę poprawnie uzupełnić dane");
            ret.setValue(createMenuDto);

            return ret;
        }

        Long creatorId = Long.valueOf(createMenuDto.getCreatorId());
        Optional<User> userOptional = userRepository.findById(creatorId);
        if (userOptional.isEmpty()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiego użytkownika");
            ret.setValue(createMenuDto);

            return ret;
        }

        Long menuTypeId = Long.valueOf(createMenuDto.getMenuTypeId());
        Optional<MenuType> menuTypeOptional = menuTypeRepository.findById(menuTypeId);
        if (menuTypeOptional.isEmpty()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiego rodzaju menu");
            ret.setValue(createMenuDto);

            return ret;
        }

        Long restaurantId = Long.valueOf(createMenuDto.getRestaurantId());
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isEmpty()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiej restauracji");
            ret.setValue(createMenuDto);

            return ret;
        }


        createMenuDto.setMenuType(menuTypeOptional.get());
        Menu prepareMenu = Menu.of(createMenuDto);
        prepareMenu.setRestaurant_menu(restaurantOptional.get());
        Menu createdMenu = menuRepository.save(prepareMenu);

        ret.setStatus(1);
        ret.setValue(CreatedMenuDto.of(createdMenu));

        return ret;
    }


}
