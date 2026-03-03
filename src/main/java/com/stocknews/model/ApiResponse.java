package com.stocknews.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Generic paginated API response wrapper.
 *
 * @param <T> the type of items in the response
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private int totalResults;
    private List<T> data;

    public static <T> ApiResponse<T> ok(List<T> data, int total) {
        return ApiResponse.<T>builder()
                .success(true)
                .totalResults(total)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
