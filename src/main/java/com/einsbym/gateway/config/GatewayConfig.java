package com.einsbym.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${einsbym.storage.service.url}")
    private String storageServiceUrl;

    @Value("${einsbym.api.url}")
    private String apiUrl;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/storage-service/**").uri(storageServiceUrl))
                .route(r -> r.path("/graphql/**").uri(apiUrl))
                .build();
    }

}
