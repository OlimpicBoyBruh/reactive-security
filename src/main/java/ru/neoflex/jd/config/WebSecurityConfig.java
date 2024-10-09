package ru.neoflex.jd.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;
import ru.neoflex.jd.security.AuthenticationManager;
import ru.neoflex.jd.security.BearerTokenServerAuthenticationConverter;
import ru.neoflex.jd.security.JwtHandler;



@Slf4j
@Configuration
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
    @Value("${jwt.secret}")
    private String secret;

    private final String[] publicRoutes = {"/api/v1/auth/register", "/api/v1/auth/login"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .pathMatchers(publicRoutes)
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> {
                    log.error("IN securityWebFilterChain - unauthorized error: {}", e.getMessage());
                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));

                })
                .accessDeniedHandler((swe, e) -> {
                    log.error("IN securityWebFilterChain - access denied.");
                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN));

                })
                .and()
                .addFilterAt(bearerAuthenticationWebFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private AuthenticationWebFilter bearerAuthenticationWebFilter(AuthenticationManager authenticationManager) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
        authenticationWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));

        return authenticationWebFilter;
    }
}
