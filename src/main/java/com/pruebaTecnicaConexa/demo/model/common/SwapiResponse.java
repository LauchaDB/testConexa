package com.pruebaTecnicaConexa.demo.model.common;

import lombok.Data;
import java.util.List;

@Data
public class SwapiResponse<T> {
    private String message;
    private List<T> result;
} 