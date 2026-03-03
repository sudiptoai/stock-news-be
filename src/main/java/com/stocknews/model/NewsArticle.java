package com.stocknews.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * Represents a single news article fetched from a news provider.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewsArticle {

    private String id;
    private String title;
    private String description;
    private String content;
    private String url;
    private String imageUrl;
    private String source;
    private String author;
    private Instant publishedAt;
    private NewsCategory category;
    private String sentiment;
}
