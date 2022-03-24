package com.tomaszgawel.userservice.controllers;

import com.tomaszgawel.userservice.dto.CreatedUserDTO;
import com.tomaszgawel.userservice.dto.UserDTO;
import com.tomaszgawel.userservice.entities.UserEntity;
import com.tomaszgawel.userservice.services.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController()
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {
    private final SignupService signupService;

    @PostMapping
    public ResponseEntity<CreatedUserDTO> signupUser(@RequestBody UserDTO userDTO) {
        UserEntity createdUserEntity = signupService.signupUser(userDTO);
        return ResponseEntity.created(URI.create("/api/v1/user/" + createdUserEntity.getId()))
                .body(new CreatedUserDTO(createdUserEntity.getId(), createdUserEntity.getUsername()));
    }
}
