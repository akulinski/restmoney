package com.akulinski.restmoney.dto;

import lombok.Data;

@Data
public class ExceptionDTO {
    private String message;
    private Throwable cause;
}
