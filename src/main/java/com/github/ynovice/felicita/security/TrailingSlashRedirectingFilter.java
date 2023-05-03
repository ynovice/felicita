package com.github.ynovice.felicita.security;

import com.github.ynovice.felicita.config.CorsProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class TrailingSlashRedirectingFilter extends OncePerRequestFilter {

    private final CorsProperties corsProperties;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String fullUrl = ServletUriComponentsBuilder
                .fromRequest(request)
                .build()
                .toString();


        String uri = request.getRequestURI();

        if(fullUrl.endsWith("/") && !uri.equals("/")) {

            fullUrl = fullUrl.substring(0, fullUrl.length() - 1);
            response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
            response.setHeader(HttpHeaders.LOCATION, fullUrl);
            appendCorsHeaders(request, response);

        } else if(uri.equals("")) {

            response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
            response.setHeader(HttpHeaders.LOCATION, fullUrl + "/");
            appendCorsHeaders(request, response);

        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void appendCorsHeaders(HttpServletRequest request, HttpServletResponse response) {

        String requestOrigin = request.getHeader("origin");

        if(corsProperties.getAllowedOrigins().contains(requestOrigin)) {
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Origin", requestOrigin);
        }
    }
}
