package com.tomaszgawel.userservice.services;

import com.tomaszgawel.userservice.dto.UserDTO;
import com.tomaszgawel.userservice.entities.UserEntity;
import com.tomaszgawel.userservice.exceptions.InvalidCredentialsException;
import com.tomaszgawel.userservice.exceptions.UserExistsException;
import com.tomaszgawel.userservice.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest
public class SignupServiceTest {

    @InjectMocks
    private SignupService signupService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEntity userEntity;

    private static Stream<Arguments> provideInvalidCredential() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("username", "password"),
                Arguments.of("Username", null),
                Arguments.of(null, "password"),
                Arguments.of("user!!", "Password1"),
                Arguments.of("user", "Password1")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCredential")
    public void testShouldThrowExceptionWithInvalidCredentials(String username, String password) {
        // given
        UserDTO userDTO = new UserDTO(username, password);
        // when
        InvalidCredentialsException thrownException = Assertions.assertThrows(InvalidCredentialsException.class,
                () -> signupService.signupUser(userDTO));
        // then
        Assertions.assertEquals("Invalid credentials", thrownException.getLocalizedMessage());
    }

    @Test
    public void testShouldThrowExceptionWhenUsernameExists() {
        // given
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.of(userEntity));
        UserDTO userDTO = new UserDTO("username", "Password1");
        // when
        UserExistsException thrownException = Assertions.assertThrows(UserExistsException.class,
                () -> signupService.signupUser(userDTO));
        // then
        Assertions.assertEquals("Username exists", thrownException.getLocalizedMessage());
    }

    @Test
    public void testShouldSaveUserEntityWithValidCredentials() {
        // given
        UserDTO userDTO = new UserDTO("username", "Password1");
        // when
        signupService.signupUser(userDTO);
        // then
        Mockito.verify(userRepository).save(Mockito.any(UserEntity.class));
    }
}
