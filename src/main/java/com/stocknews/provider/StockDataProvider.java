package com.stocknews.provider;

import com.stocknews.model.StockQuote;
import com.stocknews.model.StockRecommendation;

import java.util.List;

/**
 * Contract for any third-party stock data provider.
 * New providers can be added by implementing this interface and registering them in the factory.
 */
public interface StockDataProvider {

    /**
     * Returns the unique name/identifier for this provider.
     */
    String getProviderName();

    /**
     * Fetches the current stock quote for a given symbol.
     *
     * @param symbol the stock ticker symbol (e.g. "AAPL")
     * @return stock quote, or null if not found
     */
    StockQuote fetchQuote(String symbol);

    /**
     * Fetches analyst recommendations for a given stock symbol.
     *
     * @param symbol the stock ticker symbol
     * @return list of recommendations (latest periods first)
     */
    List<StockRecommendation> fetchRecommendations(String symbol);
}
