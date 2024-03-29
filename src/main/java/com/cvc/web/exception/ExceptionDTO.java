package com.cvc.web.exception;

import lombok.Data;

@Data
public class ExceptionDTO {
    private final Integer statusCode;
    private final String message;
    private String detail;
}
