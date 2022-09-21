package com.demo.demohttpclient.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
// com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `com.demo.demohttpclient.dto.ResDTO`
public class ResDTO {
    private HttpStatus httpStatus;
    private String message;

    public ResDTO(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
