package com.stocknews.controller;

import com.stocknews.model.NewsArticle;
import com.stocknews.model.NewsCategory;
import com.stocknews.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NewsController.class)
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Test
    void getNews_defaultParams_returns200() throws Exception {
        NewsArticle article = NewsArticle.builder()
                .id("1")
                .title("Global markets rally")
                .category(NewsCategory.ALL)
                .build();

        when(newsService.getNews(any(NewsCategory.class), anyInt())).thenReturn(List.of(article));

        mockMvc.perform(get("/api/v1/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Global markets rally"));
    }

    @Test
    void getNews_withCategoryParam_returns200() throws Exception {
        when(newsService.getNews(NewsCategory.GEOPOLITICAL, 10)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/news")
                        .param("category", "GEOPOLITICAL")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.totalResults").value(0));
    }

    @Test
    void getNews_invalidCategory_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/news").param("category", "INVALID_CAT"))
                .andExpect(status().isBadRequest());
    }
}
