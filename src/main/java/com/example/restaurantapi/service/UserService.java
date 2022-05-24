package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.user.*;
import com.example.restaurantapi.model.ConfirmationToken;
import com.example.restaurantapi.model.User;
import com.example.restaurantapi.repository.ConfirmationTokenRepository;
import com.example.restaurantapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author Szymon Królik
 */
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;
    private Map<String, String> validationResult = new HashMap<String, String>();


    /**
     * Save user in database
     * @author Szymon Królik
     * @param registerUserDto
     * @return createdUser
     */
    public ServiceReturn createUser(RegisterUserDto registerUserDto) {
        ServiceReturn ret = new ServiceReturn();
        validationResult.clear();
        Optional<User> optionalUser = userRepository.findByEmail(registerUserDto.getEmail());
        Optional<User> optionalUser1 = userRepository.findByPhoneNumber(registerUserDto.getPhone_number());
        if (optionalUser.isPresent() || optionalUser1.isPresent()) {
            ret.setStatus(0);
            validationResult.put("Uzytkownik", "Użytkownik o podanym numerze lub email już istnieje");
            ret.setErrorList(validationResult);
            ret.setValue(registerUserDto);
            return ret;
        }
        if (!optionalUser.isPresent()) {
            validationResult = validationService.registerValidation(registerUserDto);
            if (validationResult.size() > 0) {
                ret.setStatus(0);
                ret.setErrorList(validationResult);
                ret.setValue(registerUserDto);

                return ret;
            }

            User user = User.of(registerUserDto);
            final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            final User createdUser = userRepository.save(user);
            final ConfirmationToken confirmationToken = new ConfirmationToken(createdUser);
            try {
                confirmationTokenService.saveConfirmationToken(confirmationToken);

            } catch (Exception ex) {
                ret.setStatus(-1);
                ret.setMessage("Err. save confirmation token: " + ex.getMessage());
                return ret;
            }

            sendConfirmationToken(user.getEmail(), confirmationToken.getConfirmationToken());


//                sendConfirmationToken(user.getEmail(), confirmationToken.getConfirmationToken());

            ret.setStatus(1);
            ret.setErrorList(null);
            ret.setValue(createdUser);

        } else {

            ret.setStatus(-1);
            validationResult.put("email", "Email został już wykorzystany");
            ret.setErrorList(validationResult);
            return ret;
        }

        return ret;

    }

    /**
     * Login user
     * @author Szymon Królik
     * @param loginUserDto
     * @return user
     */
    public ServiceReturn loginUser(LoginUserDto loginUserDto) {
        ServiceReturn ret = new ServiceReturn();
        String password = "";
        String login = "";
        Optional<User> userOptional;
        validationResult.clear();

        if (!ServiceFunction.isNull(loginUserDto.getPassword())) {
            password = loginUserDto.getPassword();
        } else {
            ret.setStatus(0);
            validationResult.put("password", "Proszę uzupełnić hasło");

        }

        if (!ServiceFunction.isNull(loginUserDto.getLogin())) {
            login = loginUserDto.getLogin();
        } else {
            ret.setStatus(0);
            validationResult.put("login", "Proszę uzupełnić login");

        }

        if (validationResult.size() > 0) {
            ret.setStatus(0);
            ret.setErrorList(validationResult);
            ret.setValue( loginUserDto);
            return ret;
        }


        Object[] userExist = userExist(loginUserDto);
        if (!(Boolean)userExist[0]) {
            ret.setStatus(0);
            validationResult.put("error", "Nie znaleziono takiego użytkownika");
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
            LoggeduserDto dto = LoggeduserDto.of(user);
            ret.setStatus(1);
            ret.setValue(dto);
            return ret;
        } else {
            ret.setStatus(0);
            loginUserDto.setPassword(null);
            ret.setValue(loginUserDto);
            validationResult.put("password", "Proszę wprowadzić poprawne hasło");
            ret.setErrorList(validationResult);
        }



        return ret;


    }

    /**
     * Resend register mail confirmation
     * @author Szymon Królik
     * @param dto
     * @return only status
     */
    public ServiceReturn resendMail(ResendMailDto dto) {
        ServiceReturn ret = new ServiceReturn();
        ret = loginUser(LoginUserDto.of(dto));

        if (ret.getStatus() == 1) {
            User user = new User();
            user = (User) ret.getValue();
            final ConfirmationToken confirmationToken = new ConfirmationToken(user);
            confirmationTokenService.saveConfirmationToken(confirmationToken);

            sendConfirmationToken(user.getEmail(), confirmationToken.getConfirmationToken());
            ret.setStatus(1);
            return ret;
        }

        return ret;

    }

    /**
     * Send email with link to reset user password
     * @author Szymon Królik
     * @param email
     * @return
     */
    public ServiceReturn forgotPassword(String email) {
        ServiceReturn ret = new ServiceReturn();
        String userEmail = "";
        if (!ServiceFunction.isNull(email)) {
            userEmail = email;
        } else {
            ret.setStatus(-1);
            validationResult.put("email", "Proszę wprowadzić email");
            return ret;
        }

        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (!optionalUser.isPresent()) {
            ret.setStatus(-1);
            validationResult.put("error", "Nie znaleziono użytkownika o podanym email");

            return ret;
        }

        User user = optionalUser.get();
        final ConfirmationToken forgotPasswordToken = new ConfirmationToken(user);
        confirmationTokenService.saveConfirmationToken(forgotPasswordToken);
        sendForgotPasswordToken(user.getEmail(), forgotPasswordToken.getConfirmationToken());

        ret.setStatus(1);
        ret.setMessage("Email do zresetowania hasła został wysłany");
        return ret;


    }


    /*
    ret[0]: True = exist, False = not exist
    ret[1]: Optional User object
     */
    public Object[] userExist(LoginUserDto dto) {
        Object[] ret = new Object[2];
        User user = new User();
        Optional<User> userOptional = Optional.empty();
        userOptional = userRepository.findByEmail(dto.getLogin());
        if (!userOptional.isPresent()) {
            userOptional = userRepository.findByPhoneNumber(dto.getLogin());
            if (userOptional.isPresent()) {
                ret[0] = true;
                ret[1] = userOptional;
            } else {
                ret[0] = false;
                ret[1] = null;
            }
        } else {
            ret[0] = true;
            ret[1] = userOptional;
        }
        return ret;
    }


    /**
     * Send register confirmation token
     * @author Szymon Królik
     * @param userMail
     * @param token
     */
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

    /**
     * Send token to user for reset password
     * @author Szymon Królik
     * @param userMail
     * @param token
     */
    public void sendForgotPasswordToken(String userMail, String token) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String forgotPasswordLink = "http://127.0.0.1:8080/user/forgot-password?token=" + token;
        String mailMessage = "Kliknij w link aby zmienić hasło \n";
        simpleMailMessage.setTo(userMail);
        simpleMailMessage.setSubject("Forgot password");
        simpleMailMessage.setFrom("<MAIL>");
        simpleMailMessage.setText(mailMessage + forgotPasswordLink);

        emailService.sendEmail(simpleMailMessage);
    }

    /**
     * Allows to change user password with correct token
     * @author Szymon Królik
     * @param token
     * @param password
     * @return
     */
    public ServiceReturn changePassword(String token, String password) {
        ServiceReturn ret = new ServiceReturn();
        String newPassword = "";
        if (ServiceFunction.isNull(password)) {
            ret.setStatus(-1);
            validationResult.put("password","Proszę podać hasło");

        }

        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenRepository.findConfirmationTokenByConfirmationToken(token);
        if (!confirmationTokenOptional.isPresent()) {
            ret.setStatus(-1);
            validationResult.put("token","nie znaleziono takiego tokenu");

            return ret;
        }

        User user = confirmationTokenOptional.get().getUser();
        final String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        try {
            userRepository.save(user);
            confirmationTokenService.deleteForgotPasswordToken(confirmationTokenOptional.get().getId());
            ret.setStatus(1);
            ret.setMessage("Hasło pomyślnie zmienione");
        } catch (Exception ex)  {
            ret.setMessage("Err. change password " + ex.getMessage());
        }

        return ret;

    }

    /**
     * Set enabled on true for user after confirm activation link
     * @author Szymon Królik
     * @param confirmationToken
     */
    public void confirmUser(ConfirmationToken confirmationToken) {
        final User user = confirmationToken.getUser();

        user.setEnabled(true);
        try {
            userRepository.save(user);
        } catch (Exception ex) {

        }


        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
    }

    /**
     * Update user information
     * @author Szymon Królik
     * @param UserInformationDto
     */
    public ServiceReturn updateUser(UserInformationDto dto) {
        ServiceReturn ret = new ServiceReturn();
        Optional<User> optionalUser = userRepository.findById(dto.getId());
        if (!optionalUser.isPresent()) {
            ret.setMessage("Nie znaleziono takiego użytkownika");
            ret.setStatus(0);
            return ret;
        }
        final String encryptedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        dto.setPassword(encryptedPassword);

        try {
            User user = userRepository.save(User.updateUser(optionalUser.get(), dto));
            ret.setValue(UserInformationDto.of(user));
            ret.setStatus(1);
            return  ret;
        } catch (Exception ex) {
            ret.setMessage("Err. update user " + ex.getMessage());
            return  ret;
        }


    }

    public ServiceReturn deleteUser(int userId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            ret.setMessage("Nie znaleziono takiego użytkownika");
            ret.setStatus(0);
            return ret;
        }

        try {
            userRepository.deleteById(userId);
            ret.setStatus(1);
            ret.setMessage("Pomyslnie usunięto użytkownika");
        } catch (Exception ex) {
            ret.setStatus(-1);
            ret.setMessage("Err. delete user " + ex.getMessage());
        }
        return ret;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


}
