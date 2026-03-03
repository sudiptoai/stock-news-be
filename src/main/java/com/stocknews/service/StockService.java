package com.stocknews.service;

import com.stocknews.model.StockQuote;
import com.stocknews.model.StockRecommendation;
import com.stocknews.provider.factory.StockProviderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for fetching and caching stock data.
 * Delegates the actual data retrieval to the provider resolved by {@link StockProviderFactory}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockProviderFactory stockProviderFactory;

    /**
     * Fetches the current quote for the given stock symbol.
     * Results are cached for 5 minutes per symbol.
     *
     * @param symbol stock ticker symbol (e.g. "AAPL")
     * @return stock quote, or null if not available
     */
    @Cacheable(value = "quotes", key = "#symbol.toUpperCase()")
    public StockQuote getQuote(String symbol) {
        log.info("Fetching quote for symbol={}", symbol);
        return stockProviderFactory.getProvider().fetchQuote(symbol);
    }

    /**
     * Fetches analyst recommendations for the given stock symbol.
     * Results are cached for 1 hour per symbol.
     *
     * @param symbol stock ticker symbol (e.g. "AAPL")
     * @return list of analyst recommendations
     */
    @Cacheable(value = "recommendations", key = "#symbol.toUpperCase()")
    public List<StockRecommendation> getRecommendations(String symbol) {
        log.info("Fetching recommendations for symbol={}", symbol);
        return stockProviderFactory.getProvider().fetchRecommendations(symbol);
    }
}
