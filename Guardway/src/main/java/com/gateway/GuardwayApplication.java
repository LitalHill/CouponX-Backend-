package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;


@SpringBootApplication
public class GuardwayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuardwayApplication.class, args);
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:3000");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("customer-route",GuardwayApplication::CustomerUri)
                .route("coupon-route",GuardwayApplication::CouponUri)
                .route("multi-customer-routes",GuardwayApplication::MultiCustomerUri)
                .route("multi-coupon-routes", GuardwayApplication::MultiCouponUri)
                .route("auth-route", GuardwayApplication::AuthUri)
                .build();
    }

    private static Buildable<Route> CouponUri(PredicateSpec r) {
        return r.path("/coupons")
                .filters(f -> f.rewritePath("/coupons", "/coupons/all"))
                .uri("lb://coupon-hub");
    }

    private static Buildable<Route> MultiCouponUri(PredicateSpec r) {
        return r.path("/coupons/**")
                .filters(f -> f.rewritePath("/coupons/(?<seg>.*)", "/coupons/${seg}"))
                .uri("lb://coupon-hub");
    }

    private static Buildable<Route> AuthUri(PredicateSpec r) {

        return r.path("/auth/**")
                .filters(f -> f.rewritePath("/auth/(?<segment>.*)",
                        "/auth/${segment}"))
                .uri("lb://auth-forge");
    }

    private static Buildable<Route> MultiCustomerUri(PredicateSpec r) {
        return r.path("/customers/**")
                .filters(f -> f.rewritePath("/customers/(?<seg>.*)", "customers/${seg}"))
                .uri("lb://customer-connect");

    }

    private static Buildable<Route> CustomerUri(PredicateSpec r) {
        return r.path("/customers")
                .filters(f -> f.rewritePath("/customers", "/customers/all"))
                .uri("lb://customer-connect");
    }
}
