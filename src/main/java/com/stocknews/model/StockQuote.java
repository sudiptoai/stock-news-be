package com.stocknews.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a stock quote with price and change information.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockQuote {

    private String symbol;
    private String companyName;
    private Double price;
    private Double change;
    private Double changePercent;
    private Double high;
    private Double low;
    private Double open;
    private Double previousClose;
    private Long volume;
    private String latestTradingDay;
}
