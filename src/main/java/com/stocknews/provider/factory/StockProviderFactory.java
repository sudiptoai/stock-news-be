package com.stocknews.provider.factory;

import com.stocknews.provider.StockDataProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory for selecting the active {@link StockDataProvider}.
 *
 * <p>All beans implementing {@link StockDataProvider} are auto-discovered via Spring injection.
 * The active provider is chosen by the {@code stock.provider} configuration property
 * (defaults to {@code alphavantage}).  New providers can be added without changing this factory —
 * just implement {@link StockDataProvider} and annotate with {@code @Component}.
 */
@Component
public class StockProviderFactory {

    private final Map<String, StockDataProvider> providers;
    private final String activeProviderName;

    public StockProviderFactory(List<StockDataProvider> providerList,
                                @Value("${stock.provider:alphavantage}") String activeProviderName) {
        this.providers = providerList.stream()
                .collect(Collectors.toMap(StockDataProvider::getProviderName, Function.identity()));
        this.activeProviderName = activeProviderName;
    }

    /**
     * Returns the currently configured stock data provider.
     *
     * @throws IllegalArgumentException if the configured provider is not registered
     */
    public StockDataProvider getProvider() {
        StockDataProvider provider = providers.get(activeProviderName);
        if (provider == null) {
            throw new IllegalArgumentException(
                    "No StockDataProvider registered with name: " + activeProviderName
                    + ". Available: " + providers.keySet());
        }
        return provider;
    }

    /**
     * Returns a specific provider by name.
     */
    public StockDataProvider getProvider(String name) {
        StockDataProvider provider = providers.get(name);
        if (provider == null) {
            throw new IllegalArgumentException(
                    "No StockDataProvider registered with name: " + name
                    + ". Available: " + providers.keySet());
        }
        return provider;
    }
}
