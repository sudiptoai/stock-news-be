package com.stocknews.provider;

import com.stocknews.model.NewsArticle;
import com.stocknews.model.NewsCategory;
import com.stocknews.provider.factory.NewsProviderFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NewsProviderFactoryTest {

    @Test
    void getProvider_returnsActiveProvider() {
        NewsProvider mockProvider = new NewsProvider() {
            @Override
            public String getProviderName() { return "mock"; }

            @Override
            public List<NewsArticle> fetchNews(NewsCategory category, int pageSize) { return List.of(); }
        };

        NewsProviderFactory factory = new NewsProviderFactory(List.of(mockProvider), "mock");

        assertThat(factory.getProvider()).isSameAs(mockProvider);
    }

    @Test
    void getProvider_throwsWhenProviderNotFound() {
        NewsProvider mockProvider = new NewsProvider() {
            @Override
            public String getProviderName() { return "mock"; }

            @Override
            public List<NewsArticle> fetchNews(NewsCategory category, int pageSize) { return List.of(); }
        };

        NewsProviderFactory factory = new NewsProviderFactory(List.of(mockProvider), "nonexistent");

        assertThatThrownBy(factory::getProvider)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nonexistent");
    }

    @Test
    void getProviderByName_returnsCorrectProvider() {
        NewsProvider providerA = new NewsProvider() {
            @Override
            public String getProviderName() { return "providerA"; }

            @Override
            public List<NewsArticle> fetchNews(NewsCategory category, int pageSize) { return List.of(); }
        };
        NewsProvider providerB = new NewsProvider() {
            @Override
            public String getProviderName() { return "providerB"; }

            @Override
            public List<NewsArticle> fetchNews(NewsCategory category, int pageSize) { return List.of(); }
        };

        NewsProviderFactory factory = new NewsProviderFactory(List.of(providerA, providerB), "providerA");

        assertThat(factory.getProvider("providerB")).isSameAs(providerB);
    }
}
