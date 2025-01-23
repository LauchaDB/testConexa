package com.pruebaTecnicaConexa.demo.service.impl;

import com.pruebaTecnicaConexa.demo.config.SwApiProperties;
import com.pruebaTecnicaConexa.demo.model.*;
import com.pruebaTecnicaConexa.demo.model.common.PaginatedResponse;
import com.pruebaTecnicaConexa.demo.model.common.SearchResponse;
import com.pruebaTecnicaConexa.demo.model.common.SingleResponse;
import com.pruebaTecnicaConexa.demo.model.common.SwapiResponse;
import com.pruebaTecnicaConexa.demo.service.StarWarsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StarWarsServiceImpl implements StarWarsService {

    private final String NAME = "?name=";
    private final String TITLE = "?title=";
    private final String PAGE = "?page=";
    private final String LIMIT = "&limit=10";

    private final WebClient webClient;
    private final SwApiProperties swApiProperties;

    private <T> Mono<?> getWithFilters(String baseUrl, int page, String id, String searchTerm,
                                       String searchParamName, boolean isFilm) {

        if (id != null && !id.isEmpty()) {
            return webClient.get()
                    .uri(baseUrl + "/" + id)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<SingleResponse<T>>() {
                    })
                    .map(response -> response.getResult());
        }

        if (searchTerm != null && !searchTerm.isEmpty()) {
            return webClient.get()
                    .uri(baseUrl + searchParamName + searchTerm)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<SearchResponse<T>>() {
                    })
                    .map(response -> response.getResult());
        }

        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append(PAGE).append(page).append(LIMIT);

        if (isFilm) {
            return webClient.get()
                    .uri(urlBuilder.toString())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<SwapiResponse<Film>>() {
                    })
                    .map(response -> {
                        PaginatedResponse<Film> paginatedResponse = new PaginatedResponse<>();
                        if (response.getResult() != null) {
                            paginatedResponse.setResults(response.getResult());
                            paginatedResponse.setTotal_records(response.getResult().size());
                            paginatedResponse.setTotal_pages(1);
                            paginatedResponse.setNext(page < paginatedResponse.getTotal_pages() ?
                                    baseUrl + "?page=" + (page + 1) : null);
                            paginatedResponse.setPrevious(page > 1 ?
                                    baseUrl + "?page=" + (page - 1) : null);
                        }
                        return paginatedResponse;
                    });
        }

        return webClient.get()
                .uri(urlBuilder.toString())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginatedResponse<T>>() {
                })
                .map(response -> {
                    response.setNext(response.getNext() != null ?
                            baseUrl + "?page=" + (page + 1) : null);
                    response.setPrevious(page > 1 ?
                            baseUrl + "?page=" + (page - 1) : null);
                    return response;
                });

    }

    @Override
    public Mono<?> getFilmsWithFilters(int page, String id, String title) {
        return getWithFilters(
                swApiProperties.getUrlFilms(),
                page,
                id,
                title,
                TITLE,
                true
        );
    }

    @Override
    public Mono<?> getPeopleWithFilters(int page, String id, String name) {
        return getWithFilters(
            swApiProperties.getUrlPeople(),
            page,
            id,
            name,
            NAME,
            false
        );
    }

    @Override
    public Mono<?> getStarshipsWithFilters(int page, String id, String name) {
        return getWithFilters(
            swApiProperties.getUrlStarships(),
            page,
            id,
            name,
            NAME,
            false
        );
    }

    @Override
    public Mono<?> getVehiclesWithFilters(int page, String id, String name) {
        return getWithFilters(
            swApiProperties.getUrlVehicles(),
            page,
            id,
            name,
            NAME,
            false
        );
    }
} 