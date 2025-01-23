package com.pruebaTecnicaConexa.demo.controller;

import com.pruebaTecnicaConexa.demo.service.StarWarsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StarWarsController {

    private final StarWarsService starWarsService;

    @GetMapping("/people")
    public Mono<?> getPeople(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name) {
        return starWarsService.getPeopleWithFilters(page, id, name);
    }

    @GetMapping("/films")
    public Mono<?> getFilms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String title) {
        return starWarsService.getFilmsWithFilters(page, id, title);
    }

    @GetMapping("/starships")
    public Mono<?> getStarships(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name) {
        return starWarsService.getStarshipsWithFilters(page, id, name);
    }

    @GetMapping("/vehicles")
    public Mono<?> getVehicles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name) {
        return starWarsService.getVehiclesWithFilters(page, id, name);
    }
} 