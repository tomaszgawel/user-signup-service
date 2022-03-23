package com.tomaszgawel.userservice.dto;

import lombok.Data;

@Data
public class CreatedUserDTO {
    private final Long id;
    private final String username;
}
