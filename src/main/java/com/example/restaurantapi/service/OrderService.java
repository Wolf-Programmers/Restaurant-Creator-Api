package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.order.OrderInformationDto;
import com.example.restaurantapi.dto.order.PlaceOrderDto;
import com.example.restaurantapi.model.Cupon;
import com.example.restaurantapi.model.Item;
import com.example.restaurantapi.model.Order;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.repository.CuponRepository;
import com.example.restaurantapi.repository.ItemRepository;
import com.example.restaurantapi.repository.OrderRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private Map<String, String> validationResult = new HashMap<>();;
    private final ValidationService validationService;
    private final RestaurantRepository restaurantRepository;
    private final ItemRepository itemRepository;
    private final CuponRepository cuponRepository;
    private final OrderRepository orderRepository;
    private final CuponService cuponService;

    /**
     * * @author Szymon Królik
     * @param dto
     * @return
     */

    public ServiceReturn placeOrder(PlaceOrderDto dto) {
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();
        validationResult = validationService.placeOrderValidataion(dto);
        if (validationResult.size() > 0) {
            ret.setMessage("Błędy");
            ret.setStatus(0);
            ret.setErrorList(validationResult);
            ret.setValue(dto);
            return ret;
        }
        Optional<Restaurant> restaurantOptional  = restaurantRepository.findById(dto.getRestaurantId());
        if (restaurantOptional.isEmpty()) {
            ret.setMessage("Nie znaleziono takiej restauracji");
            ret.setStatus(0);
            return ret;
        }
        dto.setRestaurant(restaurantOptional.get());
        //Get items by id
        ServiceReturn itemsRet = getItems(dto.getItemsList());
        if (itemsRet.getStatus() == -1) {
            ret.setMessage("Niestety nie posiadamy takiego przedmiotu w menu");
            ret.setValue(0);
            return ret;
        }
        dto.setItemsListModel((List<Item>)itemsRet.getValue());

        if (dto.getCouponCode() != null && dto.getCouponCode().trim().length() > 3) {
            ServiceReturn couponUpdateRet = cuponService.updateCoupon(dto.getCouponCode());
            if (couponUpdateRet.getStatus() == 0) {
                ret.setMessage(couponUpdateRet.getMessage());
                return ret;
            }
            if (couponUpdateRet.getStatus() == -1) {
                ret.setMessage(couponUpdateRet.getMessage());
                ret.setStatus(couponUpdateRet.getStatus());
                return ret;
            }
            Double discount = (Double) couponUpdateRet.getValue();
            dto.setTotalPrice(dto.getTotalPrice() - discount);
        }

        try {
            Order order = orderRepository.save(Order.of(dto));
            ret.setValue(OrderInformationDto.of(order));
            ret.setStatus(1);
        } catch (Exception ex) {
            ret.setMessage("Err. Order problem: " + ex.getMessage());
            ret.setStatus(-1);
        }


        return ret;
    }

    public ServiceReturn getOrdersForRestaurant(int id) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (optionalRestaurant.isEmpty()) {
            ret.setMessage("Nie znaleziono takiej restauracji");
            ret.setStatus(0);
            return ret;
        }

       List<Order> orderList = orderRepository.findAllByRestaurant(optionalRestaurant.get());
        List<OrderInformationDto> orderInformationDtos = orderList.stream()
                .map(x -> OrderInformationDto.of(x)).collect(Collectors.toList());
        ret.setValue(orderInformationDtos);
        return ret;

    }

    public ServiceReturn getOrder(int id) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            ret.setMessage("Nie znaleziono takiego zamówienia");
            ret.setValue(0);
            return ret;
        }
        OrderInformationDto orderInformationDto = OrderInformationDto.of(optionalOrder.get());
        ret.setValue(orderInformationDto);
        ret.setStatus(1);
        return ret;

    }
    public ServiceReturn getItems(List<Integer> items) {
        ServiceReturn ret = new ServiceReturn();
        List<Item> itemList = new ArrayList<>();

        for (Integer i : items) {
            Optional<Item> optionalItem = itemRepository.findById(i);
            if (optionalItem.isEmpty()) {
                ret.setStatus(-1);
                return ret;
            }
            itemList.add(optionalItem.get());
        }

        ret.setStatus(1);
        ret.setValue(itemList);
        return ret;
    }

    /**
     * @author Szymon Królik
     * @param coupon
     * @return Operation status: -1 - problem with update coupon state
     */

}
