package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.cupon.CreateCuponDto;
import com.example.restaurantapi.dto.user.RegisterUserDto;
import com.example.restaurantapi.model.ConfirmationToken;
import com.example.restaurantapi.model.Cupon;
import com.example.restaurantapi.model.User;
import com.example.restaurantapi.repository.CuponRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CuponService  {
    private final CuponRepository cuponRepository;
    private Map<String,String> validationResult = new HashMap<String, String>();
    private final ValidationService validationService;

    public ServiceReturn createCupon(CreateCuponDto createCuponDto) {
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();
        Optional<Cupon> optionalCupon = cuponRepository.findByCuponCode(createCuponDto.getCuponCode());

        if (!optionalCupon.isEmpty()) {
            ret.setStatus(-1);
            ret.setMessage("Taki użytkownik już istnieje");
            return ret;
        }

        validationResult = validationService.addCuponValidation(createCuponDto);

        if (validationResult.size() > 0) {
            ret.setStatus(-1);
            ret.setErrorList(validationResult);
            ret.setValue(createCuponDto);

            return ret;
        } else {
            Cupon cupon = Cupon.of(createCuponDto);
            try {
                final Cupon createdCupon = cuponRepository.save(cupon);
                ret.setStatus(1);
                ret.setValue(createdCupon);
            } catch (Exception ex) {
                ret.setMessage("Create cupon: " + ex.getMessage());
                ret.setStatus(-1);
            }

            return ret;
        }
    }
}
