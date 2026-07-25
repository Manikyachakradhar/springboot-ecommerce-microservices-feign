package com.ecommerce.apigateway.filter;

import com.ecommerce.apigateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import java.util.List;

@Component
@RequiredArgsConstructor
public class    JwtAuthenticationFilter implements WebFilter {
private final JwtService jwtService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        try {
            String header =
                    exchange.getRequest()
                            .getHeaders()
                            .getFirst(HttpHeaders.AUTHORIZATION);

            if (header == null || !header.startsWith("Bearer ")) {
                return chain.filter(exchange);
            }

            String jwt = header.substring(7);
            if (!jwtService.isTokenValid(jwt)) {
                return chain.filter(exchange);
            }
            String username = jwtService.extractUserName(jwt);

            if (username == null) {
                return chain.filter(exchange);
            }
            String role = jwtService.extractRole(jwt);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority(role))
                    );

            return chain.filter(exchange)
                    .contextWrite(
                            ReactiveSecurityContextHolder.withAuthentication(authentication)
                    );
        } catch (Exception ex) {
            return chain.filter(exchange);
        }
    }
}
