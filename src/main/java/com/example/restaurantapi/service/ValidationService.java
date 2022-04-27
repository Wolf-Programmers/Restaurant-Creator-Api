package com.example.restaurantapi.service;


import com.example.restaurantapi.dto.cupon.CreateCuponDto;
import com.example.restaurantapi.dto.item.CreateItemDto;
import com.example.restaurantapi.dto.menu.CreateMenuDto;
import com.example.restaurantapi.dto.restaurant.AddEmployeeDto;
import com.example.restaurantapi.dto.restaurant.AddRestaurantDto;
import com.example.restaurantapi.dto.user.RegisterUserDto;
import com.example.restaurantapi.model.User;
import io.grpc.Server;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Szymon Królik
 */
@Service
public class ValidationService {

    public Map<String, String> errList = new HashMap<String, String>();

    public Map<String, String> registerValidation(RegisterUserDto dto) {
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
                errList.put("password", "Proszę uzupełnić hasło");
            }
            if (ServiceFunction.isNull(matchingPassword)) {
                errList.put("matchingPassword", "Proszę uzupełnić hasło");
            }
            if (!password.equals(matchingPassword)) {
                errList.put("matchingPassword", "Hasła się różnią");

            }
            if (ServiceFunction.isNull(email)) {
                errList.put("email", "Proszę uzupełnić email");

            }
            if (!ServiceFunction.validEmail(email)) {
                errList.put("email", "Niepoprawny format email");

            }
            if (ServiceFunction.isNull(phoneNumber)) {
                errList.put("phoneNumber", "Proszę uzupełnić numer telefonu");

            }
            if (!ServiceFunction.validPhoneNumber(phoneNumber)) {
                errList.put("phoneNumber", "Proszę podać poprawny numer telefonu");

            }
        } else {
            errList.put("error", "Proszę podać poprawny numer telefonu");
        }

        return errList;

    }

    public Map<String, String> restaurantValidation(AddRestaurantDto dto) {
        String email;
        String phoneNumber;

        if (!ServiceFunction.isNull((Object) dto)) {
            email = dto.getEmail();
            phoneNumber = dto.getPhoneNumber();

            if (ServiceFunction.isNull(email)) {
                errList.put("email", "Proszę podać email restauracji");

            }
            if (!ServiceFunction.validEmail(email)) {
                errList.put("email", "Proszę podać prawidłowy adres email restauracji");

            }
            if (ServiceFunction.isNull(phoneNumber)) {
                errList.put("phoneNumber", "Proszę podać numer telefonu restauracji");

            }
            if (!ServiceFunction.validPhoneNumber(phoneNumber)) {
                errList.put("phoneNumber", "Proszę podać prawidłowy numer telefonu restarucaji");

            }
        } else {
            errList.put("error", "Proszę podać dane restauracji");

        }

        return errList;
    }

    //TODO sprawdzic czy podane menu juz nie istanieje dla restauracji
    public Map<String,String> menuValidation(CreateMenuDto dto) {
        String name;
        int creatorId;
        int menuTypeId;
        int restaurantId;

        if (!ServiceFunction.isNull(dto)) {
            if (!ServiceFunction.isNull(dto.getName())) {
                name = dto.getName();
            } else {
                errList.put("name", "Proszę podać nazwę menu");

            }
            if (!ServiceFunction.isNull(dto.getCreatorId())) {
                creatorId = dto.getCreatorId();
            } else {
                errList.put("menu", "Proszę podać twórcę menu");

            }
            if (!ServiceFunction.isNull(dto.getMenuTypeId())) {
                menuTypeId = dto.getMenuTypeId();
            } else {
                errList.put("menuType", "Proszę podać rodzaj menu");

            }
            if (!ServiceFunction.isNull(dto.getRestaurantId())) {
                restaurantId = dto.getRestaurantId();
            } else {
                errList.put("restaurant", "Proszę wybrać restauracje");

            }
        } else {
            errList.put("error", "Proszę uzupełnić dane");

        }

        return errList;
    }


    public Map<String, String> itemValidation(CreateItemDto dto) {

        Double quantity =0.0;
        Double price = 0.0;

        if (!ServiceFunction.isNull(dto)) {
            if (ServiceFunction.isNull(dto.getTitle()))
                errList.put("title", "Proszę podać nazwę produktu");

            if (ServiceFunction.isNull(dto.getDesc()))
                errList.put("desc", "Proszę podać opis produktu");

            if (ServiceFunction.isNull(dto.getQuantity())) {
                errList.put("quantity", "Proszę podać ilość produktu");

            } else {
                quantity = dto.getQuantity();
                if (quantity <= 0.0)
                    errList.put("quantity", "Ilość produktu nie może być zerowa lub ujemna");

            }
            if (ServiceFunction.isNull(dto.getPrice())) {
                errList.put("price", "Proszę podać cenę produktu");

            } else {
                price = dto.getPrice();
                if (price < 0.0)
                    errList.put("price", "Cena produktu nie może być ujemna");

            }
            if (ServiceFunction.isNull(dto.getItemType()))
                errList.put("itemType", "Proszę podać typ produktu");


        } else {
            errList.put("error", "Proszę uzupełnić dane");

        }

        return errList;
    }

    public Map<String, String> addEmployeeValidation(AddEmployeeDto dto) {
        if (!ServiceFunction.isNull(dto)) {
            if (ServiceFunction.isNull(dto.getRestaurantId()))
                errList.put("restaurantId", "Proszę podać restauracje");


            if (ServiceFunction.isNull(dto.getLastName()))
                errList.put("lastName", "Proszę podać nazwisko pracowanika");


            if (ServiceFunction.isNull(dto.getName()))
                errList.put("name", "Proszę podać imie pracownika");


            if (ServiceFunction.isNull(dto.getEmail())) {
                errList.put("email", "Proszę podać email pracownika");

            } else {
                if (!ServiceFunction.validEmail(dto.getEmail()))
                    errList.put("email", "Proszę podać poprawny email pracownika");

            }

            if (ServiceFunction.isNull(dto.getPassword()))
                errList.put("password", "Proszę podać hasło dla pracownika");


            if (ServiceFunction.isNull(dto.getPhoneNumber())) {
                errList.put("phoneNumber", "Proszę podać numer telefonu pracownika");


            } else {
                if (!ServiceFunction.validPhoneNumber(dto.getPhoneNumber()))
                    errList.put("phoneNumber", "Proszę podać poprawny numer telefonu pracownika");

            }

            if (ServiceFunction.isNull(dto.getSalary())) {
                errList.put("salary", "Proszę podać wynagrodzenia pracownika");

            } else {
                if (dto.getSalary() <= 0.0)
                    errList.put("salary", "Proszę podać poprawne wynagrodzenie");

            }


        } else {
            errList.put("error", "Obiekt nie może być nullem");

        }

        return errList;
    }
    public Map<String, String> addCuponValidation(CreateCuponDto dto) {
        if (!ServiceFunction.isNull(dto)) {
            if (ServiceFunction.isNull(dto.getRestaurant()))
                errList.put("restaurant", "Proszę podać restauracje");


            if (ServiceFunction.isNull(dto.getCuponCode()))
                errList.put("cupon", "Proszę podać lub wygenerowac kod kuponu.");



            if (ServiceFunction.isNull(dto.getMaxUse()))
                errList.put("maxUse", "Proszę podać maksymalna liczbę użyć.");


        } else {
            errList.put("error", "Obiekt nie może być nullem");

        }

        return errList;
    }
}
