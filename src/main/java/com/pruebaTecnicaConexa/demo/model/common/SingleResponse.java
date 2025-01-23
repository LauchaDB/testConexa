package com.pruebaTecnicaConexa.demo.model.common;

import lombok.Data;

@Data
public class SingleResponse<T> {
    private String message;
    private T result;
} 