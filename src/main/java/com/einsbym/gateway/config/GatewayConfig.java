package com.einsbym.gateway.config;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.einsbym.gateway.entity.LogAccess;
import com.einsbym.gateway.repository.LogAccessRepository;

import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Value("${einsbym.storage.service.url}")
    private String storageServiceUrl;

    @Value("${einsbym.api.url}")
    private String apiUrl;

    @Autowired
    private LogAccessRepository logAccessRepository;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/storage-service/**").uri(storageServiceUrl))
                .route(r -> r.path("/graphql/**").uri(apiUrl))
                .build();
    }

    @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> {
            long startTime = System.currentTimeMillis();
            String method = exchange.getRequest().getMethod().toString();
            String path = exchange.getRequest().getURI().getPath();
            String ipAddress = InetAddress.getLoopbackAddress().getHostAddress();

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                String executionTime;
                if (duration < 1000) {
                    executionTime = duration + "ms";
                } else {
                    double durationInSeconds = duration / 1000.0;
                    executionTime = durationInSeconds + "s";
                }

                LogAccess logAccess = new LogAccess();
                logAccess.setMethod(method);
                logAccess.setPath(path);
                logAccess.setIp(ipAddress);
                logAccess.setExecutionTime(executionTime);
                logAccessRepository.save(logAccess);
            }));
        };
    }
}
