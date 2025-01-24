package com.pruebaTecnicaConexa.demo.controller;

import com.pruebaTecnicaConexa.demo.model.Film;
import com.pruebaTecnicaConexa.demo.model.People;
import com.pruebaTecnicaConexa.demo.model.common.PaginatedResponse;
import com.pruebaTecnicaConexa.demo.service.StarWarsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebFluxTest(StarWarsController.class)
@Import(StarWarsController.class)
class StarWarsControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private StarWarsService starWarsService;

    @Test
    @WithMockUser
    void getFilms_ShouldReturnPaginatedResponse() {
        PaginatedResponse<Film> mockResponse = new PaginatedResponse<>();
        doReturn(Mono.just(mockResponse))
            .when(starWarsService)
            .getFilmsWithFilters(anyInt(), anyString(), anyString());

        webTestClient.get()
                .uri("/api/v1/films?page=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaginatedResponse.class);
    }

    @Test
    @WithMockUser
    void getPeople_ShouldReturnPaginatedResponse() {
        PaginatedResponse<People> mockResponse = new PaginatedResponse<>();
        doReturn(Mono.just(mockResponse))
            .when(starWarsService)
            .getPeopleWithFilters(anyInt(), anyString(), anyString());

        webTestClient.get()
                .uri("/api/v1/people?page=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaginatedResponse.class);
    }

    @Test
    @WithMockUser
    void getFilms_WithSearchTitle_ShouldReturnFilteredResults() {
        PaginatedResponse<Film> mockResponse = new PaginatedResponse<>();
        doReturn(Mono.just(mockResponse))
            .when(starWarsService)
            .getFilmsWithFilters(anyInt(), anyString(), anyString());

        webTestClient.get()
                .uri("/api/v1/films?title=Hope")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaginatedResponse.class);
    }

    @Test
    void getFilms_WithoutAuthentication_ShouldReturnUnauthorized() {
        webTestClient.get()
                .uri("/api/v1/films")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @WithMockUser
    void getPeople_WithSearchName_ShouldReturnFilteredResults() {
        PaginatedResponse<People> mockResponse = new PaginatedResponse<>();
        doReturn(Mono.just(mockResponse))
            .when(starWarsService)
            .getPeopleWithFilters(anyInt(), anyString(), anyString());

        webTestClient.get()
                .uri("/api/v1/people?name=Luke")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaginatedResponse.class);
    }
} 