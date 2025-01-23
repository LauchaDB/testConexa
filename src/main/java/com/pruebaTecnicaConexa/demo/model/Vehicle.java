package com.pruebaTecnicaConexa.demo.model;

import lombok.Data;
import java.util.List;

@Data
public class Vehicle {
    private String uid;
    private Properties properties;

    @Data
    public static class Properties {
        private String model;
        private String vehicle_class;
        private String manufacturer;
        private String cost_in_credits;
        private String length;
        private String crew;
        private String passengers;
        private String max_atmosphering_speed;
        private String cargo_capacity;
        private String consumables;
        private String created;
        private String edited;
        private String name;
    }
} 