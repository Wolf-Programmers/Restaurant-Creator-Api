package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.cupon.CreateCuponDto;
import com.example.restaurantapi.dto.cupon.CreatedCuponDto;
import com.example.restaurantapi.model.Cupon;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.User;
import com.example.restaurantapi.repository.CuponRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import com.example.restaurantapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CuponService  {
    private final CuponRepository cuponRepository;
    private Map<String,String> validationResult = new HashMap<String, String>();
    private final ValidationService validationService;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public ServiceReturn createCupon(CreateCuponDto createCuponDto) {
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();
        Optional<Cupon> optionalCupon = cuponRepository.findByCuponCode(createCuponDto.getCuponCode());

        if (optionalCupon.isPresent()) {
            ret.setStatus(-1);
            ret.setMessage("Ten kod został już utworzony");
            return ret;
        }
        Optional<Restaurant> optionalRestasurant = restaurantRepository.findById(createCuponDto.restaurantId);
        createCuponDto.setRestaurant(optionalRestasurant.get());

        validationResult = validationService.addCuponValidation(createCuponDto);

        if (validationResult.size() > 0) {
            ret.setStatus(-1);
            ret.setErrorList(validationResult);
            ret.setValue(createCuponDto);

            return ret;
        } else {

            try {
                final Cupon createdCupon = cuponRepository.save(Cupon.of(createCuponDto));
                ret.setStatus(1);
                ret.setValue(CreatedCuponDto.of(createdCupon));
            } catch (Exception ex) {
                ret.setMessage("Create cupon: " + ex.getMessage());
                ret.setStatus(-1);
            }

            return ret;
        }
    }

    public  ServiceReturn updateCoupon(String coupon) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Cupon> optionalCupon = cuponRepository.findByCuponCode(coupon);
        if (optionalCupon.isPresent()) {
            try {
                Cupon cupon = cuponRepository.save(Cupon.updateCoupon(optionalCupon.get()));
                if (cupon.getMaxUses() == 0) {
                    deleteCoupon(cupon);
                }
                ret.setValue(cupon.getValue());
                ret.setStatus(1);
            } catch (Exception ex) {
                ret.setMessage("Err: updateCoupn: " + coupon);
                ret.setStatus(-1);
            }
        } else {
            ret.setStatus(0);
            ret.setMessage("Kupon już nie ważny");
        }
        return ret;
    }

    public void deleteCoupon(Cupon cupon) {
        try {
            cuponRepository.deleteById(cupon.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ServiceReturn getAllCoupons(int userId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<User> optionalUser = userRepository.findById(userId);
        //TODO if

        List<Restaurant> restaurantList = optionalUser.get().getRestaurants();
        List<CreatedCuponDto> retCupoon = new ArrayList<>();
        for (Restaurant res : restaurantList) {
            List<Cupon> c = cuponRepository.findCuponByRestaurant(res);
            for (Cupon cupon : c) {
                retCupoon.add(CreatedCuponDto.of(cupon));
            }
        }
        ret.setValue(retCupoon);
        return ret;
    }
}
