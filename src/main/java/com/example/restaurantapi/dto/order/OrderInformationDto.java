package com.example.restaurantapi.dto.order;

import com.example.restaurantapi.biznesobject.ItemMenuInformation;
import com.example.restaurantapi.dto.restaurant.InfoRestaurantDto;
import com.example.restaurantapi.model.Item;
import com.example.restaurantapi.model.Order;
import com.example.restaurantapi.model.Restaurant;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderInformationDto {
    public String customerName;
    public String customerCity;
    public String customerAddress;
    public Double totalPrice;
    public String couponCode;
    public List<ItemMenuInformation> itemsListModel;
    public InfoRestaurantDto restaurant;

    public static OrderInformationDto of(Order order) {
        OrderInformationDto dto = new OrderInformationDto();
        List<ItemMenuInformation> itemMenuInformations = new ArrayList<>();

        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerCity(order.getCustomerCity());
        dto.setCustomerAddress(order.getCustomerAddress());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setRestaurant(InfoRestaurantDto.of(order.getRestaurant()));
        dto.setItemsListModel(order.getItemList().stream()
        .map(x -> ItemMenuInformation.of(x)).collect(Collectors.toList()));
        dto.setRestaurant(InfoRestaurantDto.of(order.getRestaurant()));

        return dto;
    }
}
