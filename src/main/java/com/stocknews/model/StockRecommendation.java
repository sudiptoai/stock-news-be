package com.stocknews.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a stock recommendation or analyst rating.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockRecommendation {

    private String symbol;
    private String companyName;
    private String recommendation;   // BUY / HOLD / SELL
    private Double targetPrice;
    private Double currentPrice;
    private String analystFirm;
    private String period;           // e.g. "2024-03"
    private Integer strongBuy;
    private Integer buy;
    private Integer hold;
    private Integer sell;
    private Integer strongSell;
}
