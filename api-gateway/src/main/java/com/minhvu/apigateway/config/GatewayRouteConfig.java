package com.minhvu.apigateway.config;

import com.minhvu.apigateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

    @Autowired
    private JwtAuthenticationFilter filter;


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("discovery-server",
                        r -> r.path("/eureka/web")
                                .filters(f -> f.rewritePath("/eureka/web", "/"))
                                .uri("http://localhost:8761"))
                .route("discovery-server-static",
                        r -> r.path("/eureka/**")
                                .uri("http://localhost:8761"))
                .build();
    }

}
