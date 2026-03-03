package com.stocknews.controller;

import com.stocknews.model.ApiResponse;
import com.stocknews.model.StockQuote;
import com.stocknews.model.StockRecommendation;
import com.stocknews.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes stock data endpoints.
 *
 * <pre>
 * GET /api/v1/stocks/{symbol}/quote            - current stock quote
 * GET /api/v1/stocks/{symbol}/recommendations  - analyst recommendations
 * </pre>
 */
@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    /**
     * Returns the current stock quote for the given symbol.
     *
     * @param symbol stock ticker symbol (e.g. "AAPL")
     * @return stock quote wrapped in ApiResponse
     */
    @GetMapping("/{symbol}/quote")
    public ResponseEntity<ApiResponse<StockQuote>> getQuote(@PathVariable String symbol) {
        StockQuote quote = stockService.getQuote(symbol.toUpperCase());
        if (quote == null) {
            return ResponseEntity.ok(ApiResponse.error("No quote data available for: " + symbol));
        }
        return ResponseEntity.ok(ApiResponse.ok(List.of(quote), 1));
    }

    /**
     * Returns analyst recommendations for the given stock symbol.
     *
     * @param symbol stock ticker symbol (e.g. "AAPL")
     * @return list of recommendations wrapped in ApiResponse
     */
    @GetMapping("/{symbol}/recommendations")
    public ResponseEntity<ApiResponse<StockRecommendation>> getRecommendations(@PathVariable String symbol) {
        List<StockRecommendation> recommendations = stockService.getRecommendations(symbol.toUpperCase());
        return ResponseEntity.ok(ApiResponse.ok(recommendations, recommendations.size()));
    }
}
