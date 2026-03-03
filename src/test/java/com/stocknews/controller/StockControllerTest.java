package com.stocknews.controller;

import com.stocknews.model.StockQuote;
import com.stocknews.model.StockRecommendation;
import com.stocknews.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockController.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @Test
    void getQuote_existingSymbol_returns200WithQuote() throws Exception {
        StockQuote quote = StockQuote.builder()
                .symbol("AAPL")
                .price(185.50)
                .change(1.20)
                .changePercent(0.65)
                .build();

        when(stockService.getQuote("AAPL")).thenReturn(quote);

        mockMvc.perform(get("/api/v1/stocks/AAPL/quote"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].symbol").value("AAPL"))
                .andExpect(jsonPath("$.data[0].price").value(185.50));
    }

    @Test
    void getQuote_unknownSymbol_returnsErrorResponse() throws Exception {
        when(stockService.getQuote("UNKNOWN")).thenReturn(null);

        mockMvc.perform(get("/api/v1/stocks/UNKNOWN/quote"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("No quote data available for: UNKNOWN"));
    }

    @Test
    void getRecommendations_existingSymbol_returns200() throws Exception {
        StockRecommendation rec = StockRecommendation.builder()
                .symbol("AAPL")
                .recommendation("BUY")
                .period("2024-03")
                .strongBuy(12)
                .buy(8)
                .hold(4)
                .sell(1)
                .strongSell(0)
                .build();

        when(stockService.getRecommendations("AAPL")).thenReturn(List.of(rec));

        mockMvc.perform(get("/api/v1/stocks/AAPL/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.data[0].recommendation").value("BUY"));
    }

    @Test
    void getRecommendations_noData_returnsEmptyList() throws Exception {
        when(stockService.getRecommendations("XYZ")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/stocks/XYZ/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.totalResults").value(0));
    }
}
