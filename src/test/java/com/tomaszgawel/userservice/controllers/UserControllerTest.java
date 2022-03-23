package com.tomaszgawel.userservice.controllers;

import com.tomaszgawel.userservice.dto.CreatedUserDTO;
import com.tomaszgawel.userservice.dto.UserDTO;
import com.tomaszgawel.userservice.entities.UserEntity;
import com.tomaszgawel.userservice.exceptions.InvalidCredentialsException;
import com.tomaszgawel.userservice.exceptions.UserExistsException;
import com.tomaszgawel.userservice.services.SignupService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@SpringBootTest
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private SignupService signupService;

    @Mock
    UserDTO userDTO;

    @Test
    public void testShouldThrowExceptionWhenInvalidCredentialsProvided() {
        // given
        Mockito.when(signupService.signupUser(Mockito.any())).thenThrow(new InvalidCredentialsException());
        // when
        ResponseStatusException thrownException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userController.signupUser(userDTO));
        // then
        Assertions.assertEquals("Invalid credentials provided", thrownException.getReason());
        Assertions.assertEquals(HttpStatus.FORBIDDEN, thrownException.getStatus());
    }

    @Test
    public void testShouldThrowExceptionWhenUsernameExists() {
        // given
        Mockito.when(signupService.signupUser(Mockito.any())).thenThrow(new UserExistsException());
        // when
        ResponseStatusException thrownException = Assertions.assertThrows(ResponseStatusException.class,
                () -> userController.signupUser(userDTO));
        // then
        Assertions.assertEquals("Username exists", thrownException.getReason());
        Assertions.assertEquals(HttpStatus.CONFLICT, thrownException.getStatus());
    }

    @Test
    public void testReturnResponseEntityWithProperCredentials() {
        // given
        Mockito.when(signupService.signupUser(Mockito.any())).thenReturn(new UserEntity(1L, "username", "Password1"));
        // when
        ResponseEntity<CreatedUserDTO> responseEntity = userController.signupUser(userDTO);
        // then
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(URI.create("/api/v1/user/1"), responseEntity.getHeaders().getLocation());
        Assertions.assertEquals(new CreatedUserDTO(1L, "username"), responseEntity.getBody());
    }
}
