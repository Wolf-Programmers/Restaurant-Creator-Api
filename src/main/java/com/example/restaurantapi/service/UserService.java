package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.user.LoginUserDto;
import com.example.restaurantapi.dto.user.RegisterUserDto;
import com.example.restaurantapi.model.ConfirmationToken;
import com.example.restaurantapi.model.User;
import com.example.restaurantapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private List<String> validationResult = new ArrayList<>();

    public ServiceReturn createUser(RegisterUserDto registerUserDto) {
        ServiceReturn ret = new ServiceReturn();

        Optional<User> optionalUser = userRepository.findByEmail(registerUserDto.getEmail());
        if (optionalUser.isEmpty()) {
            validationResult = validationService.registerValidation(registerUserDto);
            if (validationResult.size() > 0) {
                ret.setStatus(-1);
                ret.setErrorList(validationResult);
                ret.setValue(registerUserDto);

                return ret;
            } else {
                User user = User.of(registerUserDto);
                final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
                user.setPassword(encryptedPassword);
                final User createdUser = userRepository.save(user);
                final ConfirmationToken confirmationToken = new ConfirmationToken(createdUser);
                confirmationTokenService.saveConfirmationToke(confirmationToken);

                sendConfirmationToken(user.getEmail(), confirmationToken.getConfirmationToken());

                ret.setStatus(1);
                ret.setErrorList(null);
                ret.setValue((Object) createdUser);
            }
        } else {

            ret.setStatus(-1);
            ret.setMessage("Taki użytkownik już istnieje");
            return ret;
        }

        return ret;

    }

    public ServiceReturn loginUser(LoginUserDto loginUserDto) {
        ServiceReturn ret = new ServiceReturn();
        String password = "";
        String login = "";
        Optional<User> userOptional = Optional.empty();;
        boolean isPasswordMatches = false;

        if (!ServiceFunction.isNull(loginUserDto.getPassword()))
            password = loginUserDto.getPassword();
        if (!ServiceFunction.isNull(loginUserDto.getLogin()))
            login = loginUserDto.getLogin();

        Object[] userExist = userExist(loginUserDto);
        if (!(Boolean)userExist[0]) {
            ret.setStatus(0);
            ret.setMessage("Nie znaleziono takiego użytkownika");
            return ret;
        }

        userOptional =(Optional<User>) userExist[1];
        User user = userOptional.get();
        if (!ServiceFunction.enableUser(user)) {
            ret.setStatus(0);
            ret.setMessage("Proszę najpierw aktywować swoje konto");
            return ret;
        }

        if (bCryptPasswordEncoder.matches(password,user.getPassword())) {
            ret.setStatus(1);
            ret.setValue(user);
            return ret;
        } else {
            ret.setStatus(0);
            loginUserDto.setPassword(null);
            ret.setValue(loginUserDto);
            ret.setMessage("Proszę wprowadzić poprawne hasło");
        }



        return ret;


    }

    public Object[] userExist(LoginUserDto dto) {
        Object[] ret = new Object[2];
        User user = new User();
        Optional<User> userOptional = Optional.empty();
        userOptional = userRepository.findByEmail(dto.getLogin());
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByPhoneNumber(dto.getLogin());
            if (userOptional.isPresent()) {
                ret[0] = (Object)true;
                ret[1] = (Object)userOptional;
            } else {
                ret[0] = (Object)false;
                ret[1] = (Object)null;
            }
        } else {
            ret[0] = (Object)true;
            ret[1] = (Object)userOptional;
        }
        return ret;
    }

    public void sendConfirmationToken(String userMail, String token) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String activationLink = "http://127.0.0.1:8080/user/confirm?token=" + token;
        String mailMessage = "Dziękujemy za rejestracje, proszę kliknąć w link aby aktywować konto \n";
        simpleMailMessage.setTo(userMail);
        simpleMailMessage.setSubject("Mail confirmation");
        simpleMailMessage.setFrom("<MAIL>");
        simpleMailMessage.setText(mailMessage + activationLink);

        emailService.sendEmail(simpleMailMessage);

    }

    public void confirmUser(ConfirmationToken confirmationToken) {
        final User user = confirmationToken.getUser();

        user.setEnabled(true);
        userRepository.save(user);

        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


}
