package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.order.OrderInformationDto;
import com.example.restaurantapi.dto.order.PlaceOrderDto;
import com.example.restaurantapi.dto.order.UpdateOrderStatusDto;
import com.example.restaurantapi.model.*;
import com.example.restaurantapi.repository.*;
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
    private final OrderStatusRepository orderStatusRepository;

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
        //Always 1 status
        Optional<OrderStatus> orderStatus = orderStatusRepository.findById(1);
        if (orderStatus.isEmpty()) {
            validationResult.put("Status", "Nie znaleziono takiego statusu w bazie danych");
            ret.setErrorList(validationResult);
            ret.setValue(dto);
            return ret;
        }
        dto.setOrderStatus(orderStatus.get());
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

    public ServiceReturn getOrdersForRestaurantByStatus(int restaurantId, int statusId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isEmpty()) {
            ret.setMessage("Nie znaleziono takiej restauracji");
            ret.setStatus(0);
            return ret;
        }

        Optional<OrderStatus> orderStatus = orderStatusRepository.findById(statusId);
        if (orderStatus.isEmpty()) {
            ret.setMessage("Nie znaleziono takiego statusu zamowienia");
            ret.setStatus(0);
            return ret;
        }

        List<Order> orderList = orderRepository.findAllByRestaurant(optionalRestaurant.get());
        List<OrderInformationDto> orderInformationDtos = orderList.stream().filter(x -> x.getOrderStatus().getId() == statusId)
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

    public ServiceReturn updateOrderStatus(UpdateOrderStatusDto dto) {
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(dto.getRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            validationResult.put("Restaurant", "Nie znaleziono takiej restauracji");
            ret.setErrorList(validationResult);
            ret.setValue(dto);
        }

        Optional<Order> optionalOrder = orderRepository.findById(dto.getOrderId());
        if (optionalOrder.isEmpty()) {
            validationResult.put("Order", "Nie znaleziono takiego zamowienia");
            ret.setErrorList(validationResult);
            ret.setValue(dto);
        }

        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findById(dto.getStatusId());
        if (optionalOrderStatus.isEmpty()) {
            validationResult.put("Status", "Nie znaleziono takiego statusu");
            ret.setErrorList(validationResult);
            ret.setValue(dto);
        }
        dto.setOrderStatus(optionalOrderStatus.get());
        try {
            Order order = orderRepository.save(Order.updateOrderStatus(optionalOrder.get(), dto));
            ret.setValue(OrderInformationDto.of(order));

        } catch (Exception ex) {
            ret.setMessage("Err. update status problem " + ex.getMessage());

        }

        return ret;
    }



}
