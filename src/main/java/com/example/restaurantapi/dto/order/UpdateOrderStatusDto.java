package com.example.restaurantapi.dto.order;

import com.example.restaurantapi.model.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusDto {
    public int restaurantId;
    public int orderId;
    public int statusId;
    public OrderStatus orderStatus;
}
