package com.stocknews.provider.alphavantage;

import com.stocknews.model.StockQuote;
import com.stocknews.model.StockRecommendation;
import com.stocknews.provider.StockDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Alpha Vantage implementation of {@link StockDataProvider}.
 * Uses the free Alpha Vantage API for real-time stock quotes and analyst recommendations.
 */
@Slf4j
@Component
public class AlphaVantageProvider implements StockDataProvider {

    private static final String PROVIDER_NAME = "alphavantage";
    private static final String BASE_URL = "https://www.alphavantage.co";

    private final WebClient webClient;
    private final String apiKey;

    public AlphaVantageProvider(WebClient.Builder webClientBuilder,
                                @Value("${alphavantage.api-key:demo}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.apiKey = apiKey;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public StockQuote fetchQuote(String symbol) {
        String url = UriComponentsBuilder.fromPath("/query")
                .queryParam("function", "GLOBAL_QUOTE")
                .queryParam("symbol", symbol)
                .queryParam("apikey", apiKey)
                .build()
                .toUriString();

        try {
            AlphaVantageQuoteResponse response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(AlphaVantageQuoteResponse.class)
                    .block();

            if (response == null || response.getGlobalQuote() == null) {
                log.warn("Alpha Vantage returned empty quote for symbol {}", symbol);
                return null;
            }

            return mapToStockQuote(symbol, response.getGlobalQuote());

        } catch (WebClientResponseException e) {
            log.error("Alpha Vantage HTTP error {} for symbol {}: {}", e.getStatusCode(), symbol, e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Error fetching quote from Alpha Vantage for symbol {}: {}", symbol, e.getMessage());
            return null;
        }
    }

    @Override
    public List<StockRecommendation> fetchRecommendations(String symbol) {
        // Alpha Vantage free tier provides analyst recommendation trends via RECOMMENDATION_TRENDS
        String url = UriComponentsBuilder.fromPath("/query")
                .queryParam("function", "RECOMMENDATION_TRENDS")
                .queryParam("symbol", symbol)
                .queryParam("apikey", apiKey)
                .build()
                .toUriString();

        try {
            AlphaVantageRecommendationResponse response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(AlphaVantageRecommendationResponse.class)
                    .block();

            if (response == null || response.getData() == null) {
                log.warn("Alpha Vantage returned no recommendations for symbol {}", symbol);
                return Collections.emptyList();
            }

            return response.getData().stream()
                    .map(entry -> mapToRecommendation(symbol, entry))
                    .collect(Collectors.toList());

        } catch (WebClientResponseException e) {
            log.error("Alpha Vantage HTTP error {} for recommendations {}: {}", e.getStatusCode(), symbol, e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Error fetching recommendations from Alpha Vantage for symbol {}: {}", symbol, e.getMessage());
            return Collections.emptyList();
        }
    }

    private StockQuote mapToStockQuote(String symbol,
                                       AlphaVantageQuoteResponse.GlobalQuote quote) {
        return StockQuote.builder()
                .symbol(symbol.toUpperCase())
                .price(parseDouble(quote.getPrice()))
                .change(parseDouble(quote.getChange()))
                .changePercent(parseChangePercent(quote.getChangePercent()))
                .open(parseDouble(quote.getOpen()))
                .high(parseDouble(quote.getHigh()))
                .low(parseDouble(quote.getLow()))
                .previousClose(parseDouble(quote.getPreviousClose()))
                .volume(parseLong(quote.getVolume()))
                .latestTradingDay(quote.getLatestTradingDay())
                .build();
    }

    private StockRecommendation mapToRecommendation(
            String symbol,
            AlphaVantageRecommendationResponse.RecommendationEntry entry) {

        int strongBuy = orZero(entry.getStrongBuy());
        int buy = orZero(entry.getBuy());
        int hold = orZero(entry.getHold());
        int sell = orZero(entry.getSell());
        int strongSell = orZero(entry.getStrongSell());

        String recommendation = deriveOverallRecommendation(strongBuy, buy, hold, sell, strongSell);

        return StockRecommendation.builder()
                .symbol(symbol.toUpperCase())
                .period(entry.getPeriod())
                .strongBuy(strongBuy)
                .buy(buy)
                .hold(hold)
                .sell(sell)
                .strongSell(strongSell)
                .recommendation(recommendation)
                .build();
    }

    /**
     * Derives a simple overall recommendation label from analyst vote counts.
     */
    private String deriveOverallRecommendation(int strongBuy, int buy, int hold, int sell, int strongSell) {
        int bullish = strongBuy + buy;
        int bearish = sell + strongSell;
        if (bullish > bearish && bullish > hold) {
            return "BUY";
        } else if (bearish > bullish && bearish > hold) {
            return "SELL";
        }
        return "HOLD";
    }

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseChangePercent(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            // Alpha Vantage returns e.g. "1.5000%"
            return Double.parseDouble(value.replace("%", "").trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int orZero(Integer value) {
        return value != null ? value : 0;
    }
}
