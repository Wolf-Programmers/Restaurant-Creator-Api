package com.example.restaurantapi.service;

import com.example.restaurantapi.biznesobject.MenuInformation;
import com.example.restaurantapi.biznesobject.TypeInformation;
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
        if (validationResult.size() > 0)
            return ServiceReturn.returnError("Please enter data",0, validationResult, createMenuDto );

        Optional<User> userOptional = userRepository.findById(createMenuDto.getCreatorId());
        if (!userOptional.isPresent())
            return ServiceReturn.returnError("Can't find user with given id", 0, createMenuDto);



        Optional<MenuType> menuTypeOptional = menuTypeRepository.findById(createMenuDto.getMenuTypeId());
        if (!menuTypeOptional.isPresent())
            return ServiceReturn.returnError("Can't find menu type with given id", 0, createMenuDto);



        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(createMenuDto.getRestaurantId());
        if (!restaurantOptional.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id", 0, createMenuDto);

        createMenuDto.setMenuType(menuTypeOptional.get());
        Menu prepareMenu = Menu.of(createMenuDto);
        prepareMenu.setRestaurant_menu(restaurantOptional.get());
        try {
            Menu createdMenu = menuRepository.save(prepareMenu);
            ret.setStatus(1);
            ret.setValue(CreatedMenuDto.of(createdMenu));
        } catch (Exception ex) {
            return ServiceReturn.returnError("Err. create menu " + ex.getMessage(), -1 );
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
        if (!optionalRestaurant.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id", 0, addItemToMenuDto.getRestaurantId());

        //Find item
        Optional<Item> optionalItem = itemRepository.findById(addItemToMenuDto.getItemId());
        if (!optionalItem.isPresent())
            return ServiceReturn.returnError("Can't find item with given id", 0, addItemToMenuDto.getItemId());

        //Find menu
        Optional<Menu> optionalMenu = menuRepository.findById(addItemToMenuDto.getMenuId());
        if (!optionalMenu.isPresent())
            return ServiceReturn.returnError("Can't find menu with given id", 0, addItemToMenuDto.getMenuId());

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
            return ServiceReturn.returnError("Err. add item to menu " + ex.getMessage(), -1 );
        }

        return ret;
    }

    public ServiceReturn showMenuById(int menuId, int restaurantId) {
        ServiceReturn ret = new ServiceReturn();

        //Find restaurant
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (!optionalRestaurant.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id", 0, restaurantId);

        //Find menu
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if (!optionalMenu.isPresent())
            return ServiceReturn.returnError("Can't find menu with given id", 0, menuId);

        MenuItemsDto retMenu = MenuItemsDto.of(optionalMenu.get());

        ret.setValue(retMenu);
        return ret;

    }

    public ServiceReturn showRestaurantMenus(int restaurantId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (!optionalRestaurant.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id", 0, restaurantId);
        List<Menu> menuList = optionalRestaurant.get().getMenus();
        List<MenuItemsDto> menus = menuList.stream().map(x -> MenuItemsDto.of(x)).collect(Collectors.toList());


        ret.setValue(menus);
        ret.setStatus(1);
        return ret;
    }

    public ServiceReturn showMenusByOwner(int ownerId) {
        ServiceReturn ret = new ServiceReturn();

        Optional<User> optionalUser = userRepository.findById(ownerId);
        if (!optionalUser.isPresent())
            return ServiceReturn.returnError("Can't find user with given id", 0, ownerId);

       List<Restaurant> restaurantList = optionalUser.get().getRestaurants();

        List<MenuInformation> menuList = restaurantList.stream().map(x -> x.getMenus()).collect(Collectors.toList())
                .stream().flatMap(List::stream).collect(Collectors.toList())
                .stream().map(x -> MenuInformation.of(x)).collect(Collectors.toList());
       ret.setValue(menuList);
       return ret;
    }

    public ServiceReturn getMenuTypes() {
        ServiceReturn ret = new ServiceReturn();

        List<MenuType> menuTypeList = menuTypeRepository.findAll();
        List<TypeInformation> typeInformations = menuTypeList.stream().map(x -> TypeInformation.of(x)).collect(Collectors.toList());
        ret.setValue(typeInformations);
        return ret;
    }




}
