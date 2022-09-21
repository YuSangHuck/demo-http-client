package com.demo.demohttpclient.exception;

import org.springframework.http.HttpStatus;

public class BadWebClientRequestException extends RuntimeException {
    private final int rawStatusCode;
    private final HttpStatus statusCode;
    private String statusText;

    public BadWebClientRequestException(int rawStatusCode, HttpStatus statusCode) {
        super();
        this.rawStatusCode = rawStatusCode;
        this.statusCode = statusCode;
    }

    public BadWebClientRequestException(int rawStatusCode, HttpStatus statusCode, String msg) {
        super(msg);
        this.rawStatusCode = rawStatusCode;
        this.statusCode = statusCode;
    }

    public BadWebClientRequestException(int rawStatusCode, HttpStatus statusCode, String msg, String statusText) {
        super(msg);
        this.rawStatusCode = rawStatusCode;
        this.statusCode = statusCode;
        this.statusText = statusText;
    }
}
