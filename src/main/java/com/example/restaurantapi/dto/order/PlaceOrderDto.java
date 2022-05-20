package com.example.restaurantapi.dto.order;

import com.example.restaurantapi.model.Item;
import com.example.restaurantapi.model.OrderStatus;
import com.example.restaurantapi.model.Restaurant;
import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderDto {
    public String customerName;
    public String customerCity;
    public String customerAddress;
    public Double totalPrice;
    public String couponCode;
    public List<Integer> itemsList;
    public List<Item> itemsListModel;
    public int restaurantId;
    public Restaurant restaurant;
    public OrderStatus orderStatus;
}
