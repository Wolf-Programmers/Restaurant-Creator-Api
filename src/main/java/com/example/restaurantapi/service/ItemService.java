package com.example.restaurantapi.service;

import com.example.restaurantapi.biznesobject.ItemMenuInformation;
import com.example.restaurantapi.biznesobject.TypeInformation;
import com.example.restaurantapi.dto.item.CreateItemDto;
import com.example.restaurantapi.dto.item.CreatedItemDto;
import com.example.restaurantapi.dto.item.UpdateItemDto;
import com.example.restaurantapi.model.*;
import com.example.restaurantapi.repository.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Szymon Królik
 */

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemTypeRepository itemTypeRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final UnitsRepository unitsRepository;

    private final ValidationService validationService;

    private Map<String, String> validationResult = new HashMap<String, String>();

    /**
     * Create new item for restaurant
     * @author Szymon Królik
     * @param createItemDto
     * @return
     */
    public ServiceReturn createItem(CreateItemDto createItemDto) {
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();

        validationResult = validationService.itemValidation(createItemDto);
        if (validationResult.size() > 0)
            return ServiceReturn.returnError("Validation error", -1, validationResult, createItemDto);

        Item item = Item.of(createItemDto);

        //get restaurant
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById((createItemDto.getRestaurantId()));
        if (!optionalRestaurant.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id", 0, createItemDto.getRestaurantId());

        item.setRestaurant(optionalRestaurant.get());
        //Get itemType
        Optional<ItemType> optionalItemType = itemTypeRepository.findById(createItemDto.getItemType());
        if (!optionalItemType.isPresent())
            return ServiceReturn.returnError("Can't find item type with given id", 0, createItemDto.getItemType());

        item.setItemType(optionalItemType.get());
        try {
            Item createdItem = itemRepository.save(item);
            CreatedItemDto createdItemDto = CreatedItemDto.of(createdItem);
            ret.setValue(createdItemDto);
            ret.setStatus(1);
        } catch (Exception ex) {
            return ServiceReturn.returnError("Err. create item " + ex.getMessage(), -1);

        }

        return ret;

    }

    /**
     * Find item by id in specific restaurant
     * @author Szymon Króik
     * @param itemId
     * @param restaurantId
     * @return
     */
    public ServiceReturn findItemByIdInRestaurant(int itemId, int restaurantId) {
        ServiceReturn ret = new ServiceReturn();

        //Find restaurant
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (!restaurantOptional.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id", 0, restaurantId);

        //Find item by id
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (!itemOptional.isPresent())
            return ServiceReturn.returnError("Can't find item with given id", 0, itemId);

        CreatedItemDto itemInformation = CreatedItemDto.of(itemOptional.get());
        ret.setStatus(1);
        ret.setValue(itemInformation);
        return ret;
    }

    /**
     * Find item by name in specific restaurant
     * @author Szymon Króik
     * @param itemName
     * @param restaurantId
     * @return
     */
    public ServiceReturn findItemByNameInRestaurant(String itemName, int restaurantId) {
        ServiceReturn ret = new ServiceReturn();
        List<CreatedItemDto> infoItemList = new ArrayList<>();

        //Find restaurant
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (!restaurantOptional.isPresent())
            return ServiceReturn.returnError("Can't find item with given id", 0, restaurantId);

        //Find item by name
        List<Item> optionalItem = itemRepository.findByTitleContaining(itemName);
        if (optionalItem.isEmpty())
            return ServiceReturn.returnError("Can't find items ", 0, itemName);


        infoItemList = optionalItem.stream().filter(x -> x.getRestaurant().getId() == restaurantId)
                .map(x -> CreatedItemDto.of(x)).collect(Collectors.toList());

        if (infoItemList.isEmpty())
            return ServiceReturn.returnError("Can't find items ", 0, itemName);
        ret.setValue(infoItemList);
        return ret;
    }

    /**
     * Find List<Item> by type in specific restsaurant
     * @author Szymon Królik
     * @param typeId
     * @param restaurantId
     * @return
     */
    public ServiceReturn findItemsByTypeInRestaurant(int typeId, int restaurantId) {
        ServiceReturn ret = new ServiceReturn();
        List<CreatedItemDto> itemsInfo = new ArrayList<>();

        //Get restaurant
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (!restaurantOptional.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id", 0, restaurantId);

        //Get item by type
        List<Item> itemsList = itemRepository.findByItemTypeId(typeId);
        if (itemsList.isEmpty())
            return ServiceReturn.returnError("Can't find items ", 0, typeId);

        itemsInfo = itemsList.stream().filter(x -> x.getRestaurant().getId() == restaurantId)
                .map(x -> CreatedItemDto.of(x)).collect(Collectors.toList());

        ret.setValue(itemsInfo);
        return ret;

    }

    public ServiceReturn updateItem(UpdateItemDto dto) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Item> optionalItem = itemRepository.findById(dto.getId());
        if (!optionalItem.isPresent())
            return ServiceReturn.returnError("Can't find item with given id ", 0, dto.getId());

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(dto.getRestaurantId());
        if (!optionalRestaurant.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id ", 0, dto.getId());

        if (!itemBelongToRestaurant(dto.getId(), optionalRestaurant.get().getItems()))
            return ServiceReturn.returnError("Can't find item in restaurant with given id ", 0, dto.getId());

        dto.setRestaurant(optionalRestaurant.get());

        Optional<ItemType> optionalItemType = itemTypeRepository.findById(dto.getItemTypeId());
        if (!optionalItemType.isPresent())
            return ServiceReturn.returnError("Can't find item with given id ", 0, dto.getItemTypeId());
        dto.setItemType(optionalItemType.get());
        try {
            Item item = itemRepository.save(Item.updateItem(optionalItem.get(), dto));
            ret.setValue(CreatedItemDto.of(item));
            ret.setStatus(1);
        } catch (Exception ex) {
            return ServiceReturn.returnError("Err. update item" + ex.getMessage(), -1);
        }


        return ret;


    }

    public ServiceReturn deleteItem(int itemId, int restaurantId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (!optionalItem.isPresent())
            return ServiceReturn.returnError("Can't find item with given id ", 0, itemId);

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (!optionalRestaurant.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id ", 0, restaurantId);

        if (!itemBelongToRestaurant(itemId, optionalRestaurant.get().getItems()))
            return ServiceReturn.returnError("Can't find item in restaurant with given id ", 0,restaurantId);
        try {
            itemRepository.deleteById(itemId);
            ret.setStatus(1);
            ret.setMessage("Pomyślnie usunięto przedmiot");
        } catch (Exception ex) {
            return ServiceReturn.returnError("Err. delete item" + ex.getMessage(), -1);
        }
        return ret;
    }

    public boolean itemBelongToRestaurant(int itemId, List<Item> items) {

        for (Item item : items) {
            if (itemId == item.getId())
                return true;
        }
        return false;
    }

    public ServiceReturn getItemsByOwner(int ownerId) {
        ServiceReturn ret = new ServiceReturn();

        Optional<User> optionalUser = userRepository.findById(ownerId);
        if (!optionalUser.isPresent())
            return ServiceReturn.returnError("Can't find user with given id ", 0,ownerId);

        List<Restaurant> restaurantList = optionalUser.get().getRestaurants();

        List<ItemMenuInformation> itemMenuInformationList = restaurantList.stream()
                .map(x -> x.getItems().stream().map(y -> ItemMenuInformation.of(y)).collect(Collectors.toList())).flatMap(List::stream).collect(Collectors.toList());

        ret.setValue(itemMenuInformationList);
        return ret;
    }

    public ServiceReturn getItemTypes() {
        ServiceReturn ret = new ServiceReturn();

        List<ItemType> itemTypes = itemTypeRepository.findAll();
        List<TypeInformation> typeInformations = itemTypes.stream().map(x -> TypeInformation.of(x)).collect(Collectors.toList());
        ret.setValue(typeInformations);

        return ret;
    }

    public ServiceReturn getItemUnits() {
        ServiceReturn ret = new ServiceReturn();
        List<Units> units = unitsRepository.findAll();

        ret.setValue(units);
        ret.setStatus(1);
        return ret;
    }

}
