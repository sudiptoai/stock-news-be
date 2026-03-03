package com.stocknews.controller;

import com.stocknews.model.ApiResponse;
import com.stocknews.model.NewsArticle;
import com.stocknews.model.NewsCategory;
import com.stocknews.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes market news endpoints.
 *
 * <pre>
 * GET /api/v1/news              - latest news (default category = ALL, pageSize = 20)
 * GET /api/v1/news?category=... - news filtered by category
 * GET /api/v1/news?pageSize=... - control number of results
 * </pre>
 */
@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /**
     * Returns latest market-relevant news articles.
     *
     * @param category  news category (default: ALL)
     * @param pageSize  number of articles (default: 20, max: 100)
     * @return paginated response with articles
     */
    @GetMapping
    public ResponseEntity<ApiResponse<NewsArticle>> getNews(
            @RequestParam(defaultValue = "ALL") NewsCategory category,
            @RequestParam(defaultValue = "20") int pageSize) {

        List<NewsArticle> articles = newsService.getNews(category, pageSize);
        return ResponseEntity.ok(ApiResponse.ok(articles, articles.size()));
    }
}
