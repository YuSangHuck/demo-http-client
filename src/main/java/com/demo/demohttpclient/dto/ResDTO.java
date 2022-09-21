package com.demo.demohttpclient.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResDTO {
    private HttpStatus httpStatus;
    private String message;

    public ResDTO(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
