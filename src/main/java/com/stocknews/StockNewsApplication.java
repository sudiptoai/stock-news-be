package com.stocknews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StockNewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockNewsApplication.class, args);
    }
}
