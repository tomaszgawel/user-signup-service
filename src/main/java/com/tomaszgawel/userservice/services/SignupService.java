package com.tomaszgawel.userservice.services;

import com.tomaszgawel.userservice.dto.UserDTO;
import com.tomaszgawel.userservice.entities.UserEntity;
import com.tomaszgawel.userservice.exceptions.*;
import com.tomaszgawel.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.passay.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class SignupService {
    private final UserRepository userRepository;

    public UserEntity signupUser(UserDTO userDTO) {
        if (StringUtils.isEmpty(userDTO.getUsername()) || StringUtils.isEmpty(userDTO.getPassword())) {
            throw new InvalidCredentialsException();
        }
        if (!StringUtils.isAlphanumeric(userDTO.getUsername()) || userDTO.getUsername().length() < 5) {
            throw new InvalidCredentialsException();
        }
        validatePassword(userDTO.getPassword());
        UserEntity userEntity = new UserEntity(userDTO.getUsername(), userDTO.getPassword());
        userRepository.findByUsername(userEntity.getUsername()).ifPresent(user -> {
            throw new UserExistsException();
        });
        return userRepository.save(userEntity);
    }

    private void validatePassword(String password) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, Integer.MAX_VALUE),
                new UppercaseCharacterRule(1),
                new LowercaseCharacterRule(1),
                new DigitCharacterRule(1)));
        if (!validator.validate(new PasswordData(password)).isValid()) {
            throw new InvalidCredentialsException();
        }
    }
}
