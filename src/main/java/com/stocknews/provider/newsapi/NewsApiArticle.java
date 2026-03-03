package com.stocknews.provider.newsapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Single article DTO from the NewsAPI response.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiArticle {

    private NewsApiSource source;
    private String author;
    private String title;
    private String description;
    private String url;

    @JsonProperty("urlToImage")
    private String urlToImage;

    @JsonProperty("publishedAt")
    private String publishedAt;

    private String content;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NewsApiSource {
        private String id;
        private String name;
    }
}
