package com.pruebaTecnicaConexa.demo.service;

import com.pruebaTecnicaConexa.demo.config.SwApiProperties;
import com.pruebaTecnicaConexa.demo.model.*;
import com.pruebaTecnicaConexa.demo.model.common.PaginatedResponse;
import com.pruebaTecnicaConexa.demo.model.common.SearchResponse;
import com.pruebaTecnicaConexa.demo.model.common.SingleResponse;
import com.pruebaTecnicaConexa.demo.service.impl.StarWarsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StarWarsServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private SwApiProperties swApiProperties;

    @InjectMocks
    private StarWarsServiceImpl starWarsService;

    @BeforeEach
    void setUp() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        when(swApiProperties.getUrlPeople()).thenReturn("https://swapi.tech/api/people");
        when(swApiProperties.getUrlFilms()).thenReturn("https://swapi.tech/api/films");
        when(swApiProperties.getUrlStarships()).thenReturn("https://swapi.tech/api/starships");
        when(swApiProperties.getUrlVehicles()).thenReturn("https://swapi.tech/api/vehicles");
    }

    @Test
    void whenGetPeopleWithoutFilters_thenReturnPaginatedResponse() {
        // Arrange
        PaginatedResponse<People> mockResponse = new PaginatedResponse<>();
        mockResponse.setTotal_records(10);
        mockResponse.setTotal_pages(1);
        mockResponse.setResults(Arrays.asList(new People()));
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(starWarsService.getPeopleWithFilters(1, null, null))
                .expectNextMatches(response -> {
                    PaginatedResponse<People> paginatedResponse = (PaginatedResponse<People>) response;
                    return paginatedResponse.getResults() != null 
                            && !paginatedResponse.getResults().isEmpty()
                            && paginatedResponse.getTotal_records() == 10;
                })
                .verifyComplete();
    }

    @Test
    void whenGetPeopleById_thenReturnSinglePerson() {
        // Arrange
        SingleResponse<People> mockResponse = new SingleResponse<>();
        People mockPerson = new People();
        People.Properties properties = new People.Properties();
        properties.setName("Luke Skywalker");
        mockPerson.setProperties(properties);
        mockResponse.setResult(mockPerson);
        
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(starWarsService.getPeopleWithFilters(1, "1", null))
                .expectNextMatches(response -> {
                    People person = (People) response;
                    return "Luke Skywalker".equals(person.getProperties().getName());
                })
                .verifyComplete();
    }

    @Test
    void whenSearchPeopleByName_thenReturnSearchResponse() {
        // Arrange
        SearchResponse<People> mockResponse = new SearchResponse<>();
        People mockPerson = new People();
        People.Properties properties = new People.Properties();
        properties.setName("Luke");
        mockPerson.setProperties(properties);
        mockResponse.setResult(Arrays.asList(mockPerson));
        
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(starWarsService.getPeopleWithFilters(1, null, "Luke"))
                .expectNextMatches(response -> {
                    java.util.List<People> people = (java.util.List<People>) response;
                    return people != null 
                            && !people.isEmpty() 
                            && "Luke".equals(people.get(0).getProperties().getName());
                })
                .verifyComplete();
    }

    @Test
    void whenGetFilmsWithoutFilters_thenReturnPaginatedResponse() {
        // Arrange
        PaginatedResponse<Film> mockResponse = new PaginatedResponse<>();
        mockResponse.setTotal_records(6);
        mockResponse.setTotal_pages(1);
        mockResponse.setResults(Arrays.asList(new Film()));
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(starWarsService.getFilmsWithFilters(1, null, null))
                .expectNextMatches(response -> {
                    PaginatedResponse<Film> paginatedResponse = (PaginatedResponse<Film>) response;
                    return paginatedResponse.getResults() != null 
                            && !paginatedResponse.getResults().isEmpty()
                            && paginatedResponse.getTotal_records() == 6;
                })
                .verifyComplete();
    }

    @Test
    void whenGetFilmById_thenReturnFilm() {
        // Arrange
        Film mockFilm = new Film();
        mockFilm.setTitle("A New Hope");
        when(responseSpec.bodyToMono(Film.class)).thenReturn(Mono.just(mockFilm));

        // Act & Assert
        StepVerifier.create(starWarsService.getFilmsWithFilters(1, "1", null))
                .expectNextMatches(response -> {
                    Film film = (Film) response;
                    return "A New Hope".equals(film.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void whenSearchFilmByTitle_thenReturnPaginatedResponse() {
        // Arrange
        PaginatedResponse<Film> mockResponse = new PaginatedResponse<>();
        Film mockFilm = new Film();
        mockFilm.setTitle("Hope");
        mockResponse.setResults(Arrays.asList(mockFilm));
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(starWarsService.getFilmsWithFilters(1, null, "Hope"))
                .expectNextMatches(response -> {
                    PaginatedResponse<Film> paginatedResponse = (PaginatedResponse<Film>) response;
                    return paginatedResponse.getResults() != null 
                            && paginatedResponse.getResults().get(0).getTitle().equals("Hope");
                })
                .verifyComplete();
    }
} 