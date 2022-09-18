package com.furkancelik.arizakayitsistemi.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private int status;

    private String path;

    private String message;

    private long timestamp = new Date().getTime();

    private Map<String, String> validationErrors;

    public ApiError(int status, String path, String message) {
        this.status = status;
        this.path = path;
        this.message = message;
    }
}
