package com.pruebaTecnicaConexa.demo.service;

import com.pruebaTecnicaConexa.demo.config.SwApiProperties;
import com.pruebaTecnicaConexa.demo.model.Film;
import com.pruebaTecnicaConexa.demo.model.People;
import com.pruebaTecnicaConexa.demo.model.common.PaginatedResponse;
import com.pruebaTecnicaConexa.demo.model.common.SearchResponse;
import com.pruebaTecnicaConexa.demo.model.common.SingleResponse;
import com.pruebaTecnicaConexa.demo.model.common.SwapiResponse;
import com.pruebaTecnicaConexa.demo.service.impl.StarWarsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StarWarsServiceImplTest {

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

    private StarWarsServiceImpl starWarsService;

    @BeforeEach
    void setUp() {
        starWarsService = new StarWarsServiceImpl(webClient, swApiProperties);
    }

    @Test
    void getFilmsWithFilters_WhenNoFilters_ShouldReturnPaginatedResponse() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(swApiProperties.getUrlFilms()).thenReturn("https://swapi.dev/api/films");
        
        SwapiResponse<Film> mockResponse = new SwapiResponse<>();
        Film mockFilm = new Film();
        mockFilm.setTitle("A New Hope");
        mockResponse.setResult(Arrays.asList(mockFilm));
        
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(starWarsService.getFilmsWithFilters(1, null, null))
                .expectNextMatches(response -> {
                    if (!(response instanceof PaginatedResponse)) return false;
                    PaginatedResponse<?> paginatedResponse = (PaginatedResponse<?>) response;
                    return paginatedResponse.getResults() != null && !paginatedResponse.getResults().isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void getFilmsWithFilters_WhenSearchById_ShouldReturnSingleFilm() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(swApiProperties.getUrlFilms()).thenReturn("https://swapi.dev/api/films");
        
        SingleResponse<Film> mockResponse = new SingleResponse<>();
        Film mockFilm = new Film();
        mockFilm.setTitle("A New Hope");
        mockResponse.setResult(mockFilm);

        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(starWarsService.getFilmsWithFilters(1, "1", null))
                .expectNextMatches(response -> response instanceof Film)
                .verifyComplete();
    }

    @Test
    void getPeopleWithFilters_WhenNoFilters_ShouldReturnPaginatedResponse() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(swApiProperties.getUrlPeople()).thenReturn("https://www.swapi.tech/api/people");
        
        PaginatedResponse<People> mockResponse = new PaginatedResponse<>();
        People mockPerson = new People();
        People.Properties properties = new People.Properties();
        properties.setName("Luke Skywalker");
        mockPerson.setProperties(properties);
        mockResponse.setResults(Arrays.asList(mockPerson));
        
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(starWarsService.getPeopleWithFilters(1, null, null))
                .expectNextMatches(response -> response instanceof PaginatedResponse)
                .verifyComplete();
    }

    @Test
    void getPeopleWithFilters_WhenSearchByName_ShouldReturnFilteredResults() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(swApiProperties.getUrlPeople()).thenReturn("https://www.swapi.tech/api/people");
        
        SearchResponse<People> mockResponse = new SearchResponse<>();
        People mockPerson = new People();
        People.Properties properties = new People.Properties();
        properties.setName("Luke Skywalker");
        mockPerson.setProperties(properties);
        mockResponse.setResult(Arrays.asList(mockPerson));
        
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(mockResponse));

        // Act & Assert
        StepVerifier.create(starWarsService.getPeopleWithFilters(1, null, "Luke"))
                .expectNextMatches(response -> {
                    if (!(response instanceof List)) return false;
                    List<?> results = (List<?>) response;
                    return !results.isEmpty() && results.get(0) instanceof People;
                })
                .verifyComplete();
    }
} 