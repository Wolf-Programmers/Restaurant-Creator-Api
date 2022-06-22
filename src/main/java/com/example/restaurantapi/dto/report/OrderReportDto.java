package com.example.restaurantapi.dto.report;

import com.example.restaurantapi.dto.item.ItemInformationDto;
import com.example.restaurantapi.model.Order;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderReportDto {
    private int orderNo;
    private String orderDate;
    private String customerCity;
    private double price;
    private List<ItemInformationDto> itemInformationDtoList;

    public static OrderReportDto of(Order order) {
        OrderReportDto dto = new OrderReportDto();

        String formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(order.getOrderDate().getTime());
        dto.setOrderDate(formattedDate);
        dto.setCustomerCity(order.getCustomerCity());
        dto.setPrice(order.getTotalPrice());
        dto.setItemInformationDtoList(order.getItemList().stream()
        .map(x -> ItemInformationDto.of(x))
                .collect(Collectors.toList())
        );
        dto.setOrderNo(order.getId());
        return dto;

    }
}

