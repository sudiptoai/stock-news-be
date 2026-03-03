package com.stocknews.provider.alphavantage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * DTO for the Alpha Vantage RECOMMENDATION_TRENDS response.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphaVantageRecommendationResponse {

    @JsonProperty("data")
    private List<RecommendationEntry> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecommendationEntry {

        @JsonProperty("symbol")
        private String symbol;

        @JsonProperty("period")
        private String period;

        @JsonProperty("strongBuy")
        private Integer strongBuy;

        @JsonProperty("buy")
        private Integer buy;

        @JsonProperty("hold")
        private Integer hold;

        @JsonProperty("sell")
        private Integer sell;

        @JsonProperty("strongSell")
        private Integer strongSell;
    }
}
