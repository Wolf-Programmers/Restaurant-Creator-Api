package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.item.CreateItemDto;
import com.example.restaurantapi.dto.item.CreatedItemDto;
import com.example.restaurantapi.model.Item;
import com.example.restaurantapi.model.ItemType;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.repository.ItemRepository;
import com.example.restaurantapi.repository.ItemTypeRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * @Author Szymon Królik
 */

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemTypeRepository itemTypeRepository;
    private final RestaurantRepository restaurantRepository;

    private final ValidationService validationService;

    private List<String> validationResult = new ArrayList<>();

    public ServiceReturn createItem(CreateItemDto createItemDto) {
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();

        validationResult = validationService.itemValidation(createItemDto);
        if (validationResult.size() > 0) {
            ret.setStatus(-1);
            ret.setErrorList(validationResult);
            ret.setValue(createItemDto);

            return ret;
        }

        Item item = Item.of(createItemDto);

        //get restaurant
        Long restaurantId = Long.valueOf(createItemDto.getRestaurantId());
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isEmpty()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiej restauracji");
            return ret;
        }
        item.setRestaurant(optionalRestaurant.get());
        //Get itemType
        Optional<ItemType> optionalItemType = itemTypeRepository.findById(Long.valueOf(createItemDto.getItemType()));
        if (optionalItemType.isEmpty()) {
            ret.setMessage("Nie znaleziono takiego rodzaju posiałku");
            ret.setValue(0);
            return ret;
        }

        item.setItemType(optionalItemType.get());
        Item createdItem = itemRepository.save(item);

        CreatedItemDto createdItemDto = CreatedItemDto.of(createdItem);

        ret.setValue(createdItemDto);
        return ret;

    }



    /**
     * Find item by id in specific restaurant
     * @author Szymon Króik
     * @param itemId
     * @param restaurantId
     * @return
     */
    public ServiceReturn findItemByIdInRestaurant(Long itemId, Long restaurantId) {
        ServiceReturn ret = new ServiceReturn();

        //Find restaurant
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isEmpty()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono podanej restauracji");
            return ret;
        }

        //Find item by id
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono podanego przedmiotu");
            return ret;
        }

        CreatedItemDto itemInformation = CreatedItemDto.of(itemOptional.get());
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
    public ServiceReturn findItemByNameInRestaurant(String itemName, Long restaurantId) {
        ServiceReturn ret = new ServiceReturn();
        List<CreatedItemDto> infoItemList = new ArrayList<>();

        //Find restaurant
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isEmpty()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono podanej restauracji");
            return ret;
        }

        //Find item by name
        List<Item> optionalItem = itemRepository.findByTitleContaining(itemName);
        if (optionalItem.size() == 0) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takich przedmiotów");
            return ret;
        }

        for (Item item : optionalItem) {
            if (item.getRestaurant().getId() == restaurantId) {
                CreatedItemDto infoItem = CreatedItemDto.of(item);
                infoItemList.add(infoItem);
            }

        }

        if (infoItemList.size() == 0) {
            ret.setMessage("Nie znaleziono żadnych dań w podanej restauracji");
            return ret;
        }
        ret.setValue(infoItemList);
        return ret;
    }



}
