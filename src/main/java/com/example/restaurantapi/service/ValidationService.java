package com.example.restaurantapi.service;


import com.example.restaurantapi.biznesobject.OpeningTimes;
import com.example.restaurantapi.biznesobject.RestaurantTypes;
import com.example.restaurantapi.dto.cupon.CreateCuponDto;
import com.example.restaurantapi.dto.item.CreateItemDto;
import com.example.restaurantapi.dto.menu.CreateMenuDto;
import com.example.restaurantapi.dto.employee.AddEmployeeDto;
import com.example.restaurantapi.dto.order.PlaceOrderDto;
import com.example.restaurantapi.dto.restaurant.AddRestaurantDto;
import com.example.restaurantapi.dto.restaurant.UpdateRestaurantDto;
import com.example.restaurantapi.dto.user.RegisterUserDto;
import com.example.restaurantapi.model.Menu;
import com.example.restaurantapi.repository.EmployeeRoleRepository;
import com.example.restaurantapi.repository.MenuRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Szymon Królik
 */
@Service
public class ValidationService {

    public Map<String, String> errList = new HashMap<>();
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final EmployeeRoleRepository employeeRoleRepository;

    public ValidationService(MenuRepository menuRepository, RestaurantRepository restaurantRepository, EmployeeRoleRepository employeeRoleRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.employeeRoleRepository = employeeRoleRepository;
    }

    public Map<String, String> registerValidation(RegisterUserDto dto) {
        String password;
        String matchingPassword = "";
        String email = "";
        String phoneNumber = "";

        if (!ServiceFunction.isNull(dto)) {
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

        if (!ServiceFunction.isNull(dto)) {
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
            for (OpeningTimes openingTimes : dto.getOpeningTimes()) {
                if (openingTimes.getFrom().equals("") || openingTimes.getFrom().length() == 0 ||openingTimes.getTo().equals("") || openingTimes.getTo().length() == 0)
                    errList.put("openingTimes", "Proszę podać godziny otwarcia");
            }
            if (ServiceFunction.isNull(dto.getOpeningTimes()))
                errList.put("openingTimes", "Proszę podać godziny otwarcia");

            if (ServiceFunction.isNull(dto.getRestaurantTypesList())) {
                errList.put("restaurantType", "Proszę podać rodzaj restauracji");
            } else {
                List<RestaurantTypes> restaurantTypesList = dto.getRestaurantTypesList();
                if (restaurantTypesList.isEmpty())
                    errList.put("restaurantType", "Proszę podać rodzaj restauracji");
            }
            if (ServiceFunction.isNull(dto.getAddress()))
                errList.put("address", "Proszę podać adres restauracji");

            if (ServiceFunction.isNull(dto.getCity()))
                errList.put("city", "Proszę podać miasto restauracji");

            if (ServiceFunction.isNull(dto.getName()))
                errList.put("city", "Proszę podać miasto restauracji");

            if (ServiceFunction.isNull(dto.getVoivodeship()))
                errList.put("voivodeship", "Proszę podać województwo");



        } else {
            errList.put("error", "Proszę podać dane restauracji");

        }

        return errList;
    }

    public Map<String, String> restaurantValidation(UpdateRestaurantDto dto) {
        String email;
        String phoneNumber;

        if (!ServiceFunction.isNull(dto)) {
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
            if (ServiceFunction.isNull(dto.getOpeningPeriod()))
                errList.put("openingTimes", "Proszę podać godziny otwarcia");
        } else {
            errList.put("error", "Proszę podać dane restauracji");

        }

        return errList;
    }


    public Map<String,String> menuValidation(CreateMenuDto dto) {

        if (!ServiceFunction.isNull(dto)) {
            Optional<Menu> optionalMenu = menuRepository.findByName(dto.getName());
            if (optionalMenu.isPresent()) {
                errList.put("name", "Menu o takiej nazwie już istnieje");
            }
            if (!ServiceFunction.isNull(dto.getName())) {
            } else {
                errList.put("name", "Proszę podać nazwę menu");

            }
            if (!ServiceFunction.isNull(dto.getCreatorId())) {
            } else {
                errList.put("menu", "Proszę podać twórcę menu");

            }
            if (!ServiceFunction.isNull(dto.getMenuTypeId())) {
            } else {
                errList.put("menuType", "Proszę podać rodzaj menu");

            }
            if (!ServiceFunction.isNull(dto.getRestaurantId())) {
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

            if (ServiceFunction.isNull(dto.getUnit()))
                errList.put("unit", "Proszę podać jednostkę");
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
            if (ServiceFunction.isNull(dto.getRestaurantId())) {
                errList.put("restaurantId", "Proszę podać restauracje");
            } else {
                if (!restaurantRepository.findById(dto.getRestaurantId()).isPresent())
                    errList.put("restaurantId", "Nie znaleziono takiej restauracji");
            }



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

            if (ServiceFunction.isNull(dto.getEmployeeRoleId())) {
                errList.put("employeeRoleId", "Proszę podać rolę pracownika");
            } else {
                if (!employeeRoleRepository.findById(dto.getEmployeeRoleId()).isPresent())
                    errList.put("employeeRoleId", "Nie znaleziono takiej roli");
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

            if (dto.getCuponCode().length() < 3)
                errList.put("cupon", "Kod jest za krótki");

            if (ServiceFunction.isNull(dto.getMaxUse()))
                errList.put("maxUse", "Proszę podać maksymalna liczbę użyć.");


        } else {
            errList.put("error", "Obiekt nie może być nullem");

        }

        return errList;
    }

    public Map<String, String> placeOrderValidataion(PlaceOrderDto dto) {
        errList.clear();
        if (dto == null)
            errList.put("Błąd", "Obiekt nie może być null");
        assert dto != null;
        if (ServiceFunction.isNull(dto.getCustomerName()))
            errList.put("Imię", "Proszę podać imię");
        if (ServiceFunction.isNull(dto.getCustomerCity()))
            errList.put("Miasto", "Proszę podać miasto");
        if (ServiceFunction.isNull(dto.getCustomerAddress()))
            errList.put("Adres", "Proszę podać adres");
        if (ServiceFunction.isNull(dto.getTotalPrice()))
            errList.put("Cena", "Źle obliczyłeś CENE!");
        if (ServiceFunction.isNull(dto.getItemsList()))
            errList.put("Przemdioty", "Nic nie zamówiłeś");
        if (dto.getItemsList().isEmpty())
            errList.put("Przedmioty", "Nic nie zamówiłeś");
        if (ServiceFunction.isNull(dto.getRestaurantId()))
            errList.put("Resturacje", "Podać restaucje");

        return errList;
    }


}
