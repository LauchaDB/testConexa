package com.pruebaTecnicaConexa.demo.model.common;

import lombok.Data;
import java.util.List;

@Data
public class SearchResponse<T> {
    private String message;
    private List<T> result;
} 