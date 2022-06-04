package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.cupon.CreateCuponDto;
import com.example.restaurantapi.dto.cupon.CreatedCuponDto;
import com.example.restaurantapi.dto.cupon.UpdateCouponDto;
import com.example.restaurantapi.model.Cupon;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.model.User;
import com.example.restaurantapi.repository.CuponRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import com.example.restaurantapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.hibernate.sql.Update;
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

    /**
     * Create cupon for restaurnat
     * @author Daniel Leznaik
     * @param
     * @return CreatedCuponDto
     */
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
        //TODO blad
        createCuponDto.setRestaurant(optionalRestasurant.get());

        validationResult = validationService.addCuponValidation(createCuponDto);

        if (validationResult.size() > 0) {
            ret.setStatus(-1);
            ret.setErrorList(validationResult);

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
    /**
     * Update specific coupoon
     * @author Daniel Leznaik
     * @param coupon code
     * @return cupon code
     */
    public  ServiceReturn updateCoupon(String coupon) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Cupon> optionalCupon = cuponRepository.findByCuponCode(coupon);
        if (optionalCupon.isPresent()) {
            try {
                Cupon cupon = cuponRepository.save(Cupon.updateCoupon(optionalCupon.get()));
                if (cupon.getMaxUses() == 0) {
                    deleteCoupon(cupon.getId());
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

    public ServiceReturn editCoupon(UpdateCouponDto updateCouponDto) {
        ServiceReturn ret = new ServiceReturn();

        Optional<Cupon> optionalCupon = cuponRepository.findById(updateCouponDto.getId());
        if (!optionalCupon.isPresent()) {
            ret.setMessage("Nie znaleziono takiego kuponu");
            ret.setStatus(0);
            ret.setValue(updateCouponDto);
            return ret;
        }


        if (!restaurantRepository.findById(updateCouponDto.getRestaurantId()).isPresent()) {
            ret.setMessage("Nie znaleziono takiej restauracji");
            ret.setStatus(0);
            ret.setValue(updateCouponDto);
            return ret;
        }
        updateCouponDto.setRestaurant(restaurantRepository.findById(updateCouponDto.getRestaurantId()).get());


        try {
            Cupon cupon = cuponRepository.save(Cupon.editCupon(optionalCupon.get(), updateCouponDto));
            ret.setValue(CreatedCuponDto.of(cupon));
            ret.setStatus(1);
        } catch (Exception ex) {
            ret.setStatus(-1);
            ret.setMessage(ex.getMessage());
        }

        return ret;
    }
    /**
     * Delete specific coupon
     * @author Daniel Leznaik
     * @param
     */
    public ServiceReturn deleteCoupon(int couponId) {
        ServiceReturn ret = new ServiceReturn();
        if (!cuponRepository.findById(couponId).isPresent()) {
            ret.setMessage("nie znaleziono takiego kuponu");
            ret.setStatus(0);
            return ret;
        }
        try {
            cuponRepository.deleteById(couponId);
            ret.setMessage("Usunieto kupon o id: " + couponId);
            ret.setStatus(1);
        } catch (Exception ex) {
            ret.setMessage(ex.getMessage());
            ret.setStatus(-1);

        }
        return  ret;
    }

    /**
     * Update specific restaurant
     * @author Daniel Leznaik
     * @param userId
     */
    public ServiceReturn getAllCoupons(int userId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiego właściciela");
            return ret;
        }

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
