package com.pruebaTecnicaConexa.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "swapi")
public class SwApiProperties {

    private String baseUrl = "https://www.swapi.tech/api";
    private String urlPeople = baseUrl + "/people";
    private String urlFilms = baseUrl + "/films";
    private String urlStarships = baseUrl + "/starships";
    private String urlVehicles = baseUrl + "/vehicles";
}