package com.stocknews.service;

import com.stocknews.model.StockQuote;
import com.stocknews.model.StockRecommendation;
import com.stocknews.provider.StockDataProvider;
import com.stocknews.provider.factory.StockProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockProviderFactory stockProviderFactory;

    @Mock
    private StockDataProvider stockDataProvider;

    @InjectMocks
    private StockService stockService;

    @BeforeEach
    void setUp() {
        when(stockProviderFactory.getProvider()).thenReturn(stockDataProvider);
    }

    @Test
    void getQuote_returnsQuoteFromProvider() {
        StockQuote expected = StockQuote.builder()
                .symbol("AAPL")
                .price(185.50)
                .change(1.20)
                .build();

        when(stockDataProvider.fetchQuote("AAPL")).thenReturn(expected);

        StockQuote result = stockService.getQuote("AAPL");

        assertThat(result).isNotNull();
        assertThat(result.getSymbol()).isEqualTo("AAPL");
        assertThat(result.getPrice()).isEqualTo(185.50);
        verify(stockDataProvider).fetchQuote("AAPL");
    }

    @Test
    void getQuote_returnsNullWhenProviderReturnsNull() {
        when(stockDataProvider.fetchQuote("UNKNOWN")).thenReturn(null);

        StockQuote result = stockService.getQuote("UNKNOWN");

        assertThat(result).isNull();
    }

    @Test
    void getRecommendations_returnsListFromProvider() {
        StockRecommendation rec = StockRecommendation.builder()
                .symbol("AAPL")
                .recommendation("BUY")
                .strongBuy(10)
                .buy(5)
                .hold(3)
                .sell(1)
                .strongSell(0)
                .build();

        when(stockDataProvider.fetchRecommendations("AAPL")).thenReturn(List.of(rec));

        List<StockRecommendation> result = stockService.getRecommendations("AAPL");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRecommendation()).isEqualTo("BUY");
        verify(stockDataProvider).fetchRecommendations("AAPL");
    }
}
