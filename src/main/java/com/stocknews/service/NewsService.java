package com.stocknews.service;

import com.stocknews.model.NewsArticle;
import com.stocknews.model.NewsCategory;
import com.stocknews.provider.factory.NewsProviderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for fetching and caching market news.
 * Delegates the actual data retrieval to the provider resolved by {@link NewsProviderFactory}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final NewsProviderFactory newsProviderFactory;

    /**
     * Fetches market news for the given category.
     * Results are cached for 15 minutes per category + pageSize combination.
     *
     * @param category news category
     * @param pageSize number of articles to retrieve (capped at {@value MAX_PAGE_SIZE})
     * @return list of news articles
     */
    @Cacheable(value = "news", key = "#category + '-' + #pageSize")
    public List<NewsArticle> getNews(NewsCategory category, int pageSize) {
        int size = Math.min(pageSize, MAX_PAGE_SIZE);
        log.info("Fetching news for category={}, pageSize={}", category, size);
        return newsProviderFactory.getProvider().fetchNews(category, size);
    }

    /**
     * Fetches market news with the default page size.
     *
     * @param category news category
     * @return list of news articles
     */
    public List<NewsArticle> getNews(NewsCategory category) {
        return getNews(category, DEFAULT_PAGE_SIZE);
    }
}
