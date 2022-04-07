package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.menu.AddItemToMenuDto;
import com.example.restaurantapi.dto.menu.CreateMenuDto;
import com.example.restaurantapi.dto.menu.CreatedMenuDto;
import com.example.restaurantapi.dto.menu.MenuItemsDto;
import com.example.restaurantapi.model.*;
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

    /**
     * Create new menu for restaurant
     * @author Szymon Królik
     * @param createMenuDto
     * @return
     */
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


        Optional<User> userOptional = userRepository.findById(createMenuDto.getCreatorId());
        if (userOptional.isEmpty()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiego użytkownika");
            ret.setValue(createMenuDto);

            return ret;
        }


        Optional<MenuType> menuTypeOptional = menuTypeRepository.findById(createMenuDto.getMenuTypeId());
        if (menuTypeOptional.isEmpty()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiego rodzaju menu");
            ret.setValue(createMenuDto);

            return ret;
        }


        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(createMenuDto.getRestaurantId());
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

    /**
     * Add new item to exist menu
     * @author Szymon Królik
     * @param addItemToMenuDto
     * @return
     */
    public ServiceReturn addItemToMenu(AddItemToMenuDto addItemToMenuDto) {
        ServiceReturn ret = new ServiceReturn();
        List<Item> itemsList = new ArrayList<>();

        //Find restaurant
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(addItemToMenuDto.getRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            ret.setMessage("NIe znaleziono takiej restauracji");
            return ret;
        }

        //Find item
        Optional<Item> optionalItem = itemRepository.findById(addItemToMenuDto.getItemId());
        if (optionalItem.isEmpty()) {
            ret.setMessage("Nie znaleziono takiego przedmiotu");
            return ret;
        }

        //Find menu
        Optional<Menu> optionalMenu = menuRepository.findById(addItemToMenuDto.getMenuId());
        if (optionalMenu.isEmpty()) {
            ret.setMessage("Nie znaleziono takiego menu");
            return ret;
        }

        Menu menu = optionalMenu.get();
        itemsList = menu.getMenuItems();
        itemsList.add(optionalItem.get());
        menu.setMenuItems(itemsList);
        Menu updatedMenu = menuRepository.save(menu);

        MenuItemsDto retMenu = MenuItemsDto.of(updatedMenu);

        ret.setValue(retMenu);
        return ret;
    }

    public ServiceReturn showMenuById(int menuId, int restaurantId) {
        ServiceReturn ret = new ServiceReturn();

        //Find restaurant
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isEmpty()) {
            ret.setMessage("NIe znaleziono takiej restauracji");
            return ret;
        }

        //Find menu
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if (optionalMenu.isEmpty()) {
            ret.setMessage("Nie znaleziono takiego menu");
            return ret;
        }

        MenuItemsDto retMenu = MenuItemsDto.of(optionalMenu.get());

        ret.setValue(retMenu);
        return ret;

    }




}
