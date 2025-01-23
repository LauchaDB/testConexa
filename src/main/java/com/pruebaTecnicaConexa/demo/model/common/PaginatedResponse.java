package com.pruebaTecnicaConexa.demo.model.common;

import lombok.Data;
import java.util.List;

@Data
public class PaginatedResponse<T> {
    private int total_records;
    private int total_pages;
    private String previous;
    private String next;
    private List<T> results;
} 