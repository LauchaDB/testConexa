package com.pruebaTecnicaConexa.demo.model;

import lombok.Data;
import java.util.List;

@Data
public class Film {
    private String uid;
    private Properties properties;
    private String title;

    @Data
    public static class Properties {
        private String title;
        private int episode_id;
        private String opening_crawl;
        private String director;
        private String producer;
        private String release_date;
        private String created;
        private String edited;
    }
} 