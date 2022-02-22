package com.example.restaurantapi.service;


import com.example.restaurantapi.dto.user.RegisterUserDto;
import com.example.restaurantapi.model.User;
import io.grpc.Server;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ValidationService {

    public List<String> errList = new ArrayList<>();

    public List<String> registerValidation(RegisterUserDto dto) {
        String password = "";
        String matchingPassword = "";
        String email = "";
        String phoneNumber = "";

        if (!ServiceFunction.isNull((Object) dto)) {
            password = dto.getPassword();
            matchingPassword = dto.getMatchingPassword();
            email = dto.getEmail();
            phoneNumber = dto.getPhone_number();

            if (ServiceFunction.isNull(password)) {
                errList.add("Proszę uzupełnić hasło");
            }
            if (ServiceFunction.isNull(matchingPassword)) {
                errList.add("Proszę uzupełnić hasło");
            }
            if (!password.equals(matchingPassword)) {
                errList.add("Hasła się różnią");
            }
            if (ServiceFunction.isNull(email)) {
                errList.add("Proszę uzupełnić email");
            }
            if (!ServiceFunction.validEmail(email)) {
                errList.add("Niepoprawny format email");
            }
            if (ServiceFunction.isNull(phoneNumber)) {
                errList.add("Proszę uzupełnić numer telefonu");
            }
            if (!ServiceFunction.validPhoneNumber(phoneNumber)) {
                errList.add("Proszę podać poprawny numer telefonu");
            }
        } else {
            errList.add("Proszę uzupełnić dane");
        }

        return errList;

    }
}
