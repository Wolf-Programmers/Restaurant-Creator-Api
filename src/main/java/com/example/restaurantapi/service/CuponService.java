package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.cupon.CreateCuponDto;
import com.example.restaurantapi.dto.cupon.CreatedCuponDto;
import com.example.restaurantapi.model.Cupon;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.repository.CuponRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CuponService  {
    private final CuponRepository cuponRepository;
    private Map<String,String> validationResult = new HashMap<String, String>();
    private final ValidationService validationService;
    private final RestaurantRepository restaurantRepository;

    public ServiceReturn createCupon(CreateCuponDto createCuponDto) {
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();
        Optional<Cupon> optionalCupon = cuponRepository.findByCuponCode(createCuponDto.getCuponCode());

        if (!optionalCupon.isEmpty()) {
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
}
