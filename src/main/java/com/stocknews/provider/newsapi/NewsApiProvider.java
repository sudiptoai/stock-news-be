package com.stocknews.provider.newsapi;

import com.stocknews.model.NewsArticle;
import com.stocknews.model.NewsCategory;
import com.stocknews.provider.NewsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * NewsAPI implementation of {@link NewsProvider}.
 * Calls the free NewsAPI /v2/everything endpoint to fetch market-relevant news.
 */
@Slf4j
@Component
public class NewsApiProvider implements NewsProvider {

    private static final String PROVIDER_NAME = "newsapi";
    private static final String BASE_URL = "https://newsapi.org";

    /**
     * Category-to-query keyword mapping for stock-market-relevant news.
     */
    private static final Map<NewsCategory, String> CATEGORY_QUERIES = Map.of(
            NewsCategory.GEOPOLITICAL, "geopolitical war sanctions trade war tariffs",
            NewsCategory.COMPANY_RESULTS, "earnings quarterly results profit revenue",
            NewsCategory.MARKET_EVENTS, "stock market crash rally IPO merger acquisition",
            NewsCategory.ECONOMY, "inflation interest rate GDP central bank federal reserve",
            NewsCategory.TECHNOLOGY, "technology AI semiconductor chip cloud computing",
            NewsCategory.ENERGY, "oil gas energy crude OPEC",
            NewsCategory.ALL, "stock market finance economy earnings"
    );

    private final WebClient webClient;
    private final String apiKey;

    public NewsApiProvider(WebClient.Builder webClientBuilder,
                           @Value("${newsapi.api-key:demo}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.apiKey = apiKey;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public List<NewsArticle> fetchNews(NewsCategory category, int pageSize) {
        String query = CATEGORY_QUERIES.getOrDefault(category, CATEGORY_QUERIES.get(NewsCategory.ALL));

        String url = UriComponentsBuilder.fromPath("/v2/everything")
                .queryParam("q", query)
                .queryParam("language", "en")
                .queryParam("sortBy", "publishedAt")
                .queryParam("pageSize", pageSize)
                .queryParam("apiKey", apiKey)
                .build()
                .toUriString();

        try {
            NewsApiResponse response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(NewsApiResponse.class)
                    .block();

            if (response == null || !"ok".equalsIgnoreCase(response.getStatus())) {
                log.warn("NewsAPI returned non-ok status for category {}", category);
                return Collections.emptyList();
            }

            return response.getArticles().stream()
                    .map(article -> mapToNewsArticle(article, category))
                    .collect(Collectors.toList());

        } catch (WebClientResponseException e) {
            log.error("NewsAPI HTTP error {}: {}", e.getStatusCode(), e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Error fetching news from NewsAPI for category {}: {}", category, e.getMessage());
            return Collections.emptyList();
        }
    }

    private NewsArticle mapToNewsArticle(NewsApiArticle article, NewsCategory category) {
        Instant publishedAt = null;
        if (article.getPublishedAt() != null) {
            try {
                publishedAt = Instant.parse(article.getPublishedAt());
            } catch (DateTimeParseException e) {
                log.debug("Could not parse date: {}", article.getPublishedAt());
            }
        }

        String sourceName = article.getSource() != null ? article.getSource().getName() : null;

        return NewsArticle.builder()
                .id(UUID.randomUUID().toString())
                .title(article.getTitle())
                .description(article.getDescription())
                .content(article.getContent())
                .url(article.getUrl())
                .imageUrl(article.getUrlToImage())
                .source(sourceName)
                .author(article.getAuthor())
                .publishedAt(publishedAt)
                .category(category)
                .build();
    }
}
