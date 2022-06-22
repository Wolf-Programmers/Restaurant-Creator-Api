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
public class CuponService {
    private final CuponRepository cuponRepository;
    private Map<String,String> validationResult = new HashMap<String, String>();
    private final ValidationService validationService;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final LogService logService;

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
            return ServiceReturn.returnError("Coupon already exist", -1);
        }
        Optional<Restaurant> optionalRestasurant = restaurantRepository.findById(createCuponDto.restaurantId);
        if (!optionalRestasurant.isPresent())
            return ServiceReturn.returnError("Can't find restaurant with given id", -1);

        createCuponDto.setRestaurant(optionalRestasurant.get());

        validationResult = validationService.addCuponValidation(createCuponDto);

        if (validationResult.size() > 0) {
            return ServiceReturn.returnError("Validation error", -1, validationResult);
        } else {

            try {
                final Cupon createdCupon = cuponRepository.save(Cupon.of(createCuponDto));
                ret.setStatus(1);
                ret.setValue(CreatedCuponDto.of(createdCupon));
            } catch (Exception ex) {
                return ServiceReturn.returnError("Err. create coupon: " + ex.getMessage(), -1);
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
                return ServiceReturn.returnInformation("Coupon updated", 1, cupon.getValue());
            } catch (Exception ex) {
                return ServiceReturn.returnError("Err. update coupon: " + ex.getMessage(), -1);
            }
        } else {
            return ServiceReturn.returnInformation("Coupon expired", 0);
        }
    }

    public ServiceReturn editCoupon(UpdateCouponDto updateCouponDto) {

        Optional<Cupon> optionalCupon = cuponRepository.findById(updateCouponDto.getId());
        if (!optionalCupon.isPresent())
            return ServiceReturn.returnInformation("Coupon with given id doesn't exist", 0, updateCouponDto);

        if (!restaurantRepository.findById(updateCouponDto.getRestaurantId()).isPresent())
            return ServiceReturn.returnInformation("Restaurant with given id doesn't exist", 0, updateCouponDto);

        updateCouponDto.setRestaurant(restaurantRepository.findById(updateCouponDto.getRestaurantId()).get());

        try {
            Cupon cupon = cuponRepository.save(Cupon.editCupon(optionalCupon.get(), updateCouponDto));
            return ServiceReturn.returnInformation("Coupon edited" , 1, CreatedCuponDto.of(cupon));
        } catch (Exception ex) {
            return ServiceReturn.returnError("Err. edit coupon value: " + ex.getMessage(), -1);
        }

    }
    /**
     * Delete specific coupon
     * @author Daniel Leznaik
     * @param
     */
    public ServiceReturn deleteCoupon(int couponId) {
        ServiceReturn ret = new ServiceReturn();
        if (!cuponRepository.findById(couponId).isPresent())
            return ServiceReturn.returnError("Can't find coupon with given id", 0,couponId);
        try {
            cuponRepository.deleteById(couponId);
            return ServiceReturn.returnInformation("Deleted coupon", 1, couponId);
        } catch (Exception ex) {
            return ServiceReturn.returnError("Err. delete coupon",-1, couponId);
        }

    }

    /**
     * Update specific restaurant
     * @author Daniel Leznaik
     * @param userId
     */
    public ServiceReturn getAllCoupons(int userId) {
        ServiceReturn ret = new ServiceReturn();
        List<CreatedCuponDto> retCupoon = new ArrayList<>();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent())
            return ServiceReturn.returnError("Can't find owner with given id", 0, userId);


        List<Restaurant> restaurantList = optionalUser.get().getRestaurants();

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
