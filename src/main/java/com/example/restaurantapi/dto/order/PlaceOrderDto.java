package com.example.restaurantapi.dto.order;

import lombok.Data;

@Data
public class PlaceOrderDto {
    public String customerName;
    public String customerCity;
    public String customerAddress;
    public Double totalPrice;
    public String couponCode;

}
