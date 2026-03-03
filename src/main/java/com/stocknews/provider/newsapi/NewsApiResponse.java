package com.stocknews.provider.newsapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Top-level response DTO from the NewsAPI /v2/everything endpoint.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiResponse {

    private String status;
    private Integer totalResults;
    private List<NewsApiArticle> articles;
}
