package com.example.restaurantapi.model;

import com.example.restaurantapi.dto.order.PlaceOrderDto;
import com.example.restaurantapi.dto.order.UpdateOrderStatusDto;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "orderItem")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private String customerName;
    @Column(nullable = false)
    private String customerCity;
    @Column(nullable = false)
    private String customerAddress;
    @Column(nullable = false)
    private Double totalPrice;
    @Column(nullable = false)
    private String couponCode;

    @OneToOne(targetEntity = Restaurant.class)
    @JoinColumn(nullable = false, name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "item_order_table",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> itemList;

    @ManyToOne
    private OrderStatus orderStatus;

    public static Order of(PlaceOrderDto dto) {
        Order order = new Order();
        order.setCustomerName(dto.getCustomerName());
        order.setCustomerCity(dto.getCustomerCity());
        order.setCustomerAddress(dto.getCustomerAddress());
        order.setTotalPrice(dto.getTotalPrice());
        order.setTotalPrice(dto.getTotalPrice());
        order.setCouponCode(dto.getCouponCode());
        order.setRestaurant(dto.getRestaurant());
        order.setItemList(dto.getItemsListModel());
        order.setOrderStatus(dto.getOrderStatus());

        return order;
    }

    public static Order updateOrderStatus(Order order, UpdateOrderStatusDto orderStatus) {
        order.setOrderStatus(orderStatus.getOrderStatus());

        return order;
    }

}
