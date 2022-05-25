package com.example.restaurantapi.service;

import com.example.restaurantapi.biznesobject.MenuInformation;
import com.example.restaurantapi.dto.menu.AddItemToMenuDto;
import com.example.restaurantapi.dto.menu.CreateMenuDto;
import com.example.restaurantapi.dto.menu.CreatedMenuDto;
import com.example.restaurantapi.dto.menu.MenuItemsDto;
import com.example.restaurantapi.model.*;
import com.example.restaurantapi.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    private Map<String, String> validationResult = new HashMap<String, String>();

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
        if (!userOptional.isPresent()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiego użytkownika");
            ret.setValue(createMenuDto);

            return ret;
        }


        Optional<MenuType> menuTypeOptional = menuTypeRepository.findById(createMenuDto.getMenuTypeId());
        if (!menuTypeOptional.isPresent()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiego rodzaju menu");
            ret.setValue(createMenuDto);

            return ret;
        }


        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(createMenuDto.getRestaurantId());
        if (!restaurantOptional.isPresent()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiej restauracji");
            ret.setValue(createMenuDto);

            return ret;
        }


        createMenuDto.setMenuType(menuTypeOptional.get());
        Menu prepareMenu = Menu.of(createMenuDto);
        prepareMenu.setRestaurant_menu(restaurantOptional.get());
        try {
            Menu createdMenu = menuRepository.save(prepareMenu);

            ret.setStatus(1);
            ret.setValue(CreatedMenuDto.of(createdMenu));
        } catch (Exception ex) {
            ret.setMessage("Err. create menu " + ex.getMessage());
            ret.setStatus(-1);
        }


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
        if (!optionalRestaurant.isPresent()) {
            ret.setMessage("NIe znaleziono takiej restauracji");
            return ret;
        }

        //Find item
        Optional<Item> optionalItem = itemRepository.findById(addItemToMenuDto.getItemId());
        if (!optionalItem.isPresent()) {
            ret.setMessage("Nie znaleziono takiego przedmiotu");
            return ret;
        }

        //Find menu
        Optional<Menu> optionalMenu = menuRepository.findById(addItemToMenuDto.getMenuId());
        if (!optionalMenu.isPresent()) {
            ret.setMessage("Nie znaleziono takiego menu");
            return ret;
        }

        Menu menu = optionalMenu.get();
        itemsList = menu.getMenuItems();
        itemsList.add(optionalItem.get());
        menu.setMenuItems(itemsList);
        Menu updatedMenu = menuRepository.save(menu);

        try {
            MenuItemsDto retMenu = MenuItemsDto.of(updatedMenu);

            ret.setValue(retMenu);
            ret.setStatus(1);
        } catch (Exception ex) {
            ret.setMessage("Err. add item to menu "  + ex.getMessage());
            ret.setStatus(-1);
        }

        return ret;
    }

    public ServiceReturn showMenuById(int menuId, int restaurantId) {
        ServiceReturn ret = new ServiceReturn();

        //Find restaurant
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (!optionalRestaurant.isPresent()) {
            ret.setMessage("NIe znaleziono takiej restauracji");
            return ret;
        }

        //Find menu
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if (!optionalMenu.isPresent()) {
            ret.setMessage("Nie znaleziono takiego menu");
            return ret;
        }

        MenuItemsDto retMenu = MenuItemsDto.of(optionalMenu.get());

        ret.setValue(retMenu);
        return ret;

    }

    public ServiceReturn showRestaurantMenus(int restaurantId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (!optionalRestaurant.isPresent()) {
            ret.setMessage("Nie znaleziono takiej restauracji");
            ret.setStatus(0);
            return ret;
        }
        List<Menu> menuList = optionalRestaurant.get().getMenus();
        List<MenuItemsDto> menus = menuList.stream().map(x -> MenuItemsDto.of(x)).collect(Collectors.toList());


        ret.setValue(menus);
        ret.setStatus(1);
        return ret;
    }





}
