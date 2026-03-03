package com.stocknews.provider;

import com.stocknews.model.StockQuote;
import com.stocknews.model.StockRecommendation;
import com.stocknews.provider.factory.StockProviderFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockProviderFactoryTest {

    @Test
    void getProvider_returnsActiveProvider() {
        StockDataProvider mockProvider = new StockDataProvider() {
            @Override
            public String getProviderName() { return "mock"; }

            @Override
            public StockQuote fetchQuote(String symbol) { return null; }

            @Override
            public List<StockRecommendation> fetchRecommendations(String symbol) { return List.of(); }
        };

        StockProviderFactory factory = new StockProviderFactory(List.of(mockProvider), "mock");

        assertThat(factory.getProvider()).isSameAs(mockProvider);
    }

    @Test
    void getProvider_throwsWhenProviderNotFound() {
        StockDataProvider mockProvider = new StockDataProvider() {
            @Override
            public String getProviderName() { return "mock"; }

            @Override
            public StockQuote fetchQuote(String symbol) { return null; }

            @Override
            public List<StockRecommendation> fetchRecommendations(String symbol) { return List.of(); }
        };

        StockProviderFactory factory = new StockProviderFactory(List.of(mockProvider), "missing");

        assertThatThrownBy(factory::getProvider)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("missing");
    }
}
