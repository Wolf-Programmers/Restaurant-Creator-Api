package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.item.ItemInformationDto;
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
        if (validationResult.size() > 0)
            return ServiceReturn.returnError("Validation error", 0, validationResult, dto);

        Optional<Restaurant> restaurantOptional  = restaurantRepository.findById(dto.getRestaurantId());
        if (!restaurantOptional.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id" , dto.getRestaurantId());
        dto.setRestaurant(restaurantOptional.get());
        //Get items by id
        ServiceReturn itemsRet = getItems(dto.getItemsList());
        if (itemsRet.getStatus() == -1)
            return ServiceReturn.returnInformation("Can't find item with given id" , dto.getRestaurantId());

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
        if (!orderStatus.isPresent()) {
            validationResult.put("Status", "Nie znaleziono takiego statusu w bazie danych");
            return ServiceReturn.returnError("Validation error", 0, validationResult, dto);
        }

        dto.setOrderStatus(orderStatus.get());
        try {
            Order order = orderRepository.save(Order.of(dto));
            ret.setValue(OrderInformationDto.of(order));
            ret.setStatus(1);
        } catch (Exception ex) {
            return ServiceReturn.returnError("Err. place order " + ex.getMessage() , -1);
        }


        return ret;
    }

    public ServiceReturn getOrdersForRestaurant(int id) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (!optionalRestaurant.isPresent())
            return ServiceReturn.returnInformation("Can't find restaurant with given id" , id);

       List<Order> orderList = orderRepository.findAllByRestaurant(optionalRestaurant.get());
        List<OrderInformationDto> orderInformationDtos = orderList.stream()
                .map(x -> OrderInformationDto.of(x)).collect(Collectors.toList());
        ret.setValue(orderInformationDtos);
        return ret;

    }

    public ServiceReturn getOrdersForRestaurantByStatus(int restaurantId, int statusId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (!optionalRestaurant.isPresent())
            return ServiceReturn.returnInformation("Can't find restaurant with given id" , restaurantId);

        Optional<OrderStatus> orderStatus = orderStatusRepository.findById(statusId);
        if (!orderStatus.isPresent())
            return ServiceReturn.returnInformation("Can't find order status with given id" , statusId);

        List<Order> orderList = orderRepository.findAllByRestaurant(optionalRestaurant.get());
        List<OrderInformationDto> orderInformationDtos = orderList.stream().filter(x -> x.getOrderStatus().getId() == statusId)
                .map(x -> OrderInformationDto.of(x)).collect(Collectors.toList());
        ret.setValue(orderInformationDtos);
        return ret;

    }

    public ServiceReturn getOrder(int id) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent())
            return ServiceReturn.returnInformation("Can't find order  with given id" , id);
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
            if (!optionalItem.isPresent()) {
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
        if (!optionalRestaurant.isPresent()) {
            validationResult.put("Restaurant", "Nie znaleziono takiej restauracji");
            ret.setErrorList(validationResult);
            ret.setValue(dto);
        }

        Optional<Order> optionalOrder = orderRepository.findById(dto.getOrderId());
        if (!optionalOrder.isPresent()) {
            validationResult.put("Order", "Nie znaleziono takiego zamowienia");
            ret.setErrorList(validationResult);
            ret.setValue(dto);
        }

        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findById(dto.getStatusId());
        if (!optionalOrderStatus.isPresent())
            return ServiceReturn.returnInformation("Can't find order status with given id" , dto.getStatusId());
        dto.setOrderStatus(optionalOrderStatus.get());
        try {
            Order order = orderRepository.save(Order.updateOrderStatus(optionalOrder.get(), dto));
            ret.setValue(OrderInformationDto.of(order));

        } catch (Exception ex) {
            return ServiceReturn.returnError("Err. update order status " + ex.getMessage() , -1);

        }

        return ret;
    }




}
