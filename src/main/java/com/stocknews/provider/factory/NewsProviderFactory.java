package com.stocknews.provider.factory;

import com.stocknews.provider.NewsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory for selecting the active {@link NewsProvider}.
 *
 * <p>All beans implementing {@link NewsProvider} are auto-discovered via Spring injection.
 * The active provider is chosen by the {@code news.provider} configuration property
 * (defaults to {@code newsapi}).  New providers can be added without changing this factory —
 * just implement {@link NewsProvider} and annotate with {@code @Component}.
 */
@Component
public class NewsProviderFactory {

    private final Map<String, NewsProvider> providers;
    private final String activeProviderName;

    public NewsProviderFactory(List<NewsProvider> providerList,
                               @Value("${news.provider:newsapi}") String activeProviderName) {
        this.providers = providerList.stream()
                .collect(Collectors.toMap(NewsProvider::getProviderName, Function.identity()));
        this.activeProviderName = activeProviderName;
    }

    /**
     * Returns the currently configured news provider.
     *
     * @throws IllegalArgumentException if the configured provider is not registered
     */
    public NewsProvider getProvider() {
        NewsProvider provider = providers.get(activeProviderName);
        if (provider == null) {
            throw new IllegalArgumentException(
                    "No NewsProvider registered with name: " + activeProviderName
                    + ". Available: " + providers.keySet());
        }
        return provider;
    }

    /**
     * Returns a specific provider by name.
     */
    public NewsProvider getProvider(String name) {
        NewsProvider provider = providers.get(name);
        if (provider == null) {
            throw new IllegalArgumentException(
                    "No NewsProvider registered with name: " + name
                    + ". Available: " + providers.keySet());
        }
        return provider;
    }
}
