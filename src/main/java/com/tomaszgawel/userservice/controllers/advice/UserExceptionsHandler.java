package com.tomaszgawel.userservice.controllers.advice;

import com.tomaszgawel.userservice.dto.ErrorMessageDTO;
import com.tomaszgawel.userservice.exceptions.InvalidCredentialsException;
import com.tomaszgawel.userservice.exceptions.UserExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UserExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UserExistsException.class })
    protected ResponseEntity<Object> handleUserExistsException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ErrorMessageDTO("Username exists"), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {InvalidCredentialsException.class })
    protected ResponseEntity<Object> handleInvalidCredentialsException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ErrorMessageDTO("Invalid credentials provided"), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
}
