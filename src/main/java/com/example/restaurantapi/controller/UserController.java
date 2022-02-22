package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.user.LoginUserDto;
import com.example.restaurantapi.dto.user.RegisterUserDto;
import com.example.restaurantapi.model.ConfirmationToken;
import com.example.restaurantapi.repository.ConfirmationTokenRepository;
import com.example.restaurantapi.service.ServiceReturn;
import com.example.restaurantapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ConfirmationTokenRepository confirmationTokenRepository;


    @PostMapping("/register")
    public ServiceReturn registerUser(@RequestBody RegisterUserDto dto) {
        ServiceReturn ret = userService.createUser(dto);

        return ret;
    }

    @PostMapping("/login")
    public ServiceReturn loginUser(@RequestBody LoginUserDto dto) {
        ServiceReturn ret = userService.loginUser(dto);

        return ret;
    }

    @GetMapping("/confirm")
    public String confirmMail(@RequestParam("token") String token) {

        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenRepository.findConfirmationTokenByConfirmationToken(token);

        confirmationTokenOptional.ifPresent(userService::confirmUser);

        return "Udało się potwiedzić konto!";
    }


}
