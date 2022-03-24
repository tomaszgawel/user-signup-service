package com.tomaszgawel.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomaszgawel.userservice.dto.CreatedUserDTO;
import com.tomaszgawel.userservice.dto.ErrorMessageDTO;
import com.tomaszgawel.userservice.dto.UserDTO;
import com.tomaszgawel.userservice.entities.UserEntity;
import com.tomaszgawel.userservice.exceptions.InvalidCredentialsException;
import com.tomaszgawel.userservice.exceptions.UserExistsException;
import com.tomaszgawel.userservice.services.SignupService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private SignupService signupService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testReturnStatus403WhenInvalidCredentialsProvided() throws Exception {
        Mockito.when(signupService.signupUser(Mockito.any()))
                .thenThrow(new InvalidCredentialsException());
        mockMvc.perform(post("/api/v1/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new UserDTO("user", "password"))))
                .andExpect(status().isForbidden())
                .andExpect(content().json(objectMapper.writeValueAsString(new ErrorMessageDTO("Invalid credentials provided"))));
    }

    @Test
    public void testReturnStatus403WhenNullCredentialsProvided() throws Exception {
        Mockito.when(signupService.signupUser(Mockito.any()))
                .thenThrow(new InvalidCredentialsException());
        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDTO(null, null))))
                .andExpect(status().isForbidden())
                .andExpect(content().json(objectMapper.writeValueAsString(new ErrorMessageDTO("Invalid credentials provided"))));
    }

    @Test
    public void testReturnStatus201WhenValidCredentialsProvided() throws Exception {
        Mockito.when(signupService.signupUser(Mockito.any()))
                .thenReturn(new UserEntity(1L, "username", "Password1"));
        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDTO("username", "Password1"))))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(new CreatedUserDTO(1L, "username"))))
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/v1/user/1"));
    }
    @Test
    public void testReturnStatus409WhenUsernameExists() throws Exception {
        Mockito.when(signupService.signupUser(Mockito.any()))
                .thenThrow(new UserExistsException());
        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDTO("username", "Password1"))))
                .andExpect(status().isConflict())
                .andExpect(content().json(objectMapper.writeValueAsString(new ErrorMessageDTO("Username exists"))));
    }
}
