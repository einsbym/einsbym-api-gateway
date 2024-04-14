package com.einsbym.api.gateway.config;

import com.einsbym.api.gateway.entity.RequestLog;
import com.einsbym.api.gateway.repository.RequestLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.net.InetAddress;

@Configuration
public class GatewayConfig {

    @Value("${einsbym.storage.service.url}")
    private String storageServiceUrl;

    @Value("${einsbym.api.url}")
    private String apiUrl;

    @Autowired
    private RequestLogRepository requestLogRepository;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/storage-service/**").uri(storageServiceUrl))
                .route(r -> r.path("/**").uri(apiUrl))
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

                RequestLog requestLog = new RequestLog();
                requestLog.setMethod(method);
                requestLog.setPath(path);
                requestLog.setIp(ipAddress);
                requestLog.setExecutionTime(executionTime);
                requestLogRepository.save(requestLog);
            }));
        };
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(false);
        corsConfig.addAllowedOrigin("*"); // Allow all origins
        corsConfig.addAllowedMethod("*"); // Allow all HTTP methods
        corsConfig.addAllowedHeader("*"); // Allow all headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
