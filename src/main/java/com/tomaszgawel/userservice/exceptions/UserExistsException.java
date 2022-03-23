package com.tomaszgawel.userservice.exceptions;

public class UserExistsException extends RuntimeException{
    public UserExistsException() {
        super("Username exists");
    }
}
