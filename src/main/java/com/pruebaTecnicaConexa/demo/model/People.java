package com.pruebaTecnicaConexa.demo.model;

import lombok.Data;

@Data
public class People {

    private String uid;

    private Properties properties;

    @Data
    public static class Properties {

        private String height;

        private String mass;

        private String hair_color;

        private String skin_color;

        private String eye_color;

        private String birth_year;

        private String gender;

        private String created;

        private String edited;

        private String name;
    }
} 