package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.restaurant.CreateTableDto;
import com.example.restaurantapi.dto.restaurant.TableInformationDto;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.RestaurantTable;
import com.example.restaurantapi.repository.RestaurantRepository;
import com.example.restaurantapi.repository.TableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;
    private final ValidationService validationService;
    Map<String, String> validationResult = new HashMap<>();

    public ServiceReturn createTable(CreateTableDto dto) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(dto.getRestaurantId());
        if (optionalRestaurant.isEmpty())
            return ServiceReturn.returnError("Can't find restaurant with gven id",0, dto.getRestaurantId());

        validationResult = validationService.createTableValidation(dto);
        if (!validationResult.isEmpty())
            return ServiceReturn.returnError("Validation error", 0, validationResult, dto);

        RestaurantTable table = new RestaurantTable();
        table.setCapacity(dto.getCapacity());
        table.setNumber(dto.getNumber());
        table.setRestaurant(optionalRestaurant.get());
        try  {
            TableInformationDto createdTable = TableInformationDto.of(tableRepository.save(table));
            return ServiceReturn.returnInformation("Created table", 1,createdTable);
        } catch (Exception e) {
            return ServiceReturn.returnError("Err. create table " + e.getMessage(),-1);
        }

    }

    public ServiceReturn getTable(int id) {
        Optional<RestaurantTable> optionalRestaurantTable = tableRepository.findById(id);
        if (optionalRestaurantTable.isEmpty())
            return ServiceReturn.returnError("Can't find table with given id",0);

        return ServiceReturn.returnInformation("",1, TableInformationDto.of(optionalRestaurantTable.get()));
    }

    public ServiceReturn getTableByRestaurantId(int restaurantId) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isEmpty())
            return ServiceReturn.returnError("Cen't find restaurant with given id",0);

        if (optionalRestaurant.get().getTables().isEmpty())
            return ServiceReturn.returnError("Can't find tables in restaurant", 0);

        List<TableInformationDto> tableList = optionalRestaurant.get().getTables()
                .stream()
                .map(x -> TableInformationDto.of(x))
                .collect(Collectors.toList());

        return ServiceReturn.returnInformation("",1,tableList);
    }

    public ServiceReturn editTable(CreateTableDto dto) {
        Optional<RestaurantTable> optionalRestaurantTable = tableRepository.findById(dto.getTableId()) ;

        if (optionalRestaurantTable.isEmpty())
            return ServiceReturn.returnError("Can't find table with given id", 0,dto);

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(dto.getRestaurantId());
        if (optionalRestaurantTable.isEmpty())
            return ServiceReturn.returnError("Can't find table with given id", 0,dto);

        dto.setRestaurant(optionalRestaurant.get());

        RestaurantTable table = RestaurantTable.update(optionalRestaurantTable.get(), dto);
        try {
            TableInformationDto updatetable = TableInformationDto.of(tableRepository.save(table));
            return ServiceReturn.returnInformation("Updated", 1, updatetable);
        } catch (Exception e) {
            return ServiceReturn.returnError("Err. update table " + e.getMessage(), -1);
        }



    }
}
