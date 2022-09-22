package com.furkancelik.arizakayitsistemi.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthException extends RuntimeException{

    public AuthException() {
        super();
    }

    public AuthException(String message) {
        super(message);
    }
}
