package com.stocknews.provider;

import com.stocknews.model.NewsArticle;
import com.stocknews.model.NewsCategory;

import java.util.List;

/**
 * Contract for any third-party news data provider.
 * New providers can be added by implementing this interface and registering them in the factory.
 */
public interface NewsProvider {

    /**
     * Returns the unique name/identifier for this provider.
     */
    String getProviderName();

    /**
     * Fetches the latest market-relevant news articles.
     *
     * @param category the news category to filter by
     * @param pageSize maximum number of articles to return
     * @return list of news articles
     */
    List<NewsArticle> fetchNews(NewsCategory category, int pageSize);
}
