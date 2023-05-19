package com.github.ynovice.felicita.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsProperties corsProperties;

    private final OAuth2UserService<OidcUserRequest, OidcUser> customOidcUserService;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService;

    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CsrfTokenRequestHandler customCsrfTokenRequestHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors()
                    .and()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/csrf").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/material/**",
                                "/api/category/**",
                                "/api/color/**",
                                "/api/image/**",
                                "/api/size/**",
                                "/api/item/**").permitAll()
                        .anyRequest().authenticated())
                .logout(l -> l

                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout"))
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
                .csrf(c -> c
                        .csrfTokenRequestHandler(customCsrfTokenRequestHandler)
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint().oidcUserService(customOidcUserService)
                        .userService(customOAuth2UserService)
                        .and()
                        .successHandler(customAuthenticationSuccessHandler))
                .build();
    }

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(corsProperties.getAllowedOrigins());
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
