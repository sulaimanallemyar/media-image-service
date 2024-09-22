package com.example.image_service.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestAlertException extends RuntimeException {

    public BadRequestAlertException(String message){
        super(message);
    }
}
