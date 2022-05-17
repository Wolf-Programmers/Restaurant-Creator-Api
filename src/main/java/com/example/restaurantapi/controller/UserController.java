package com.example.restaurantapi.controller;

import com.example.restaurantapi.dto.user.LoginUserDto;
import com.example.restaurantapi.dto.user.RegisterUserDto;
import com.example.restaurantapi.dto.user.ResendMailDto;
import com.example.restaurantapi.dto.user.UserInformationDto;
import com.example.restaurantapi.model.ConfirmationToken;
import com.example.restaurantapi.model.User;
import com.example.restaurantapi.repository.ConfirmationTokenRepository;
import com.example.restaurantapi.service.ServiceReturn;
import com.example.restaurantapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @Author Szymon Królik
 */
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@CrossOrigin
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

    @PostMapping("/resend-mail")
    public ServiceReturn resendMail(@RequestBody ResendMailDto dto) {
        ServiceReturn ret = userService.resendMail(dto);

        return ret;
    }

    @GetMapping("/forgot-password")
    public ServiceReturn forgotPassword(@RequestParam("mail") String mail) {
        ServiceReturn ret = userService.forgotPassword(mail);

        return ret;
    }

    @PostMapping("/forgot-password")
    public ServiceReturn passwordChange(@RequestParam("token") String token, @RequestBody LoginUserDto dto) {

        ServiceReturn ret = userService.changePassword(token, dto.getPassword());

        return ret;
    }
    @GetMapping("/confirm")
    public String confirmMail(@RequestParam("token") String token) {

        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenRepository.findConfirmationTokenByConfirmationToken(token);

        confirmationTokenOptional.ifPresent(userService::confirmUser);

        return "Udało się potwiedzić konto!";
    }
    @PutMapping("/update")
    public ServiceReturn passwordChange(@RequestBody UserInformationDto dto) {
        return userService.updateUser(dto);
    }

    @DeleteMapping("/delete")
    public ServiceReturn deleteUser(@RequestParam("userId") int userId) {
        return userService.deleteUser(userId);
    }


}
