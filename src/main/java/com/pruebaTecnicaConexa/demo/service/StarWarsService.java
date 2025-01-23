package com.pruebaTecnicaConexa.demo.service;

import reactor.core.publisher.Mono;

public interface StarWarsService {
    Mono<?> getPeopleWithFilters(int page, String id, String name);

    Mono<?> getFilmsWithFilters(int page, String id, String title);

    Mono<?> getStarshipsWithFilters(int page, String id, String name);

    Mono<?> getVehiclesWithFilters(int page, String id, String name);
} 