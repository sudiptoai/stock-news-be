package com.stocknews.service;

import com.stocknews.model.NewsArticle;
import com.stocknews.model.NewsCategory;
import com.stocknews.provider.NewsProvider;
import com.stocknews.provider.factory.NewsProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsProviderFactory newsProviderFactory;

    @Mock
    private NewsProvider newsProvider;

    @InjectMocks
    private NewsService newsService;

    @BeforeEach
    void setUp() {
        when(newsProviderFactory.getProvider()).thenReturn(newsProvider);
    }

    @Test
    void getNews_returnArticlesFromProvider() {
        NewsArticle article = NewsArticle.builder()
                .id("1")
                .title("Market rally continues")
                .category(NewsCategory.MARKET_EVENTS)
                .build();

        when(newsProvider.fetchNews(NewsCategory.MARKET_EVENTS, 20)).thenReturn(List.of(article));

        List<NewsArticle> result = newsService.getNews(NewsCategory.MARKET_EVENTS, 20);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Market rally continues");
        verify(newsProvider).fetchNews(NewsCategory.MARKET_EVENTS, 20);
    }

    @Test
    void getNews_capsPageSizeAtMax() {
        when(newsProvider.fetchNews(any(), anyInt())).thenReturn(List.of());

        newsService.getNews(NewsCategory.ALL, 200);

        verify(newsProvider).fetchNews(NewsCategory.ALL, 100);
    }

    @Test
    void getNews_defaultCategory_returnsArticles() {
        NewsArticle article = NewsArticle.builder().id("2").title("Fed rate decision").build();
        when(newsProvider.fetchNews(any(), anyInt())).thenReturn(List.of(article));

        List<NewsArticle> result = newsService.getNews(NewsCategory.ECONOMY);

        assertThat(result).hasSize(1);
    }
}
