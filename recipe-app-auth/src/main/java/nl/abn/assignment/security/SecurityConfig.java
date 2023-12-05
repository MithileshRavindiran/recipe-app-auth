/*
 * Copyright (c) mithilesh89ece@gmail.com. All Rights Reserved.
 * ============================================================
 */
package nl.abn.assignment.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import nl.abn.assignment.filter.AuthorizationFilter;
import nl.abn.assignment.filter.CustomAuthenticationFilter;
import nl.abn.assignment.service.MongoAuthUserDetailService;
import nl.abn.assignment.service.TokenParserService;


/**
 * To Enable the Authorization and Authentication based on the resource and the role assigned to the user
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled=true,prePostEnabled=true,jsr250Enabled=true)
public class SecurityConfig {
    private static final String[] SWAGGER_WHITELIST = {
        // -- Swagger UI v2
        "/v3/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**",
        // -- Swagger UI v3 (OpenAPI)
        "/v3/api-docs/**",
        "/swagger-ui/**"
        // other public endpoints of your API may be appended to this array
    };

    private final MongoAuthUserDetailService userDetailsService;

    private final AuthenticationManagerBuilder authManagerBuilder;

    private final TokenParserService tokenParserService;

    @Autowired
    public SecurityConfig(MongoAuthUserDetailService userDetailsService, AuthenticationManagerBuilder authManagerBuilder, TokenParserService tokenParserService) {
        this.userDetailsService = userDetailsService;
        this.authManagerBuilder = authManagerBuilder;
        this.tokenParserService = tokenParserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject
            (AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }


    /**
     * To Allow or block the user from accessing the resource
     *
     * @param http request information
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests((authz) -> authz
            .requestMatchers("/login/**").permitAll()
            .requestMatchers("/console/**").permitAll()
            .requestMatchers(SWAGGER_WHITELIST).permitAll()
            .requestMatchers("/recipes/**").permitAll()
            .requestMatchers("/admin/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
            .anyRequest().authenticated())
            .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilter(new CustomAuthenticationFilter(authManagerBuilder.getOrBuild(), tokenParserService))
            .addFilterBefore(customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthorizationFilter customAuthorizationFilter() throws Exception {
        return new AuthorizationFilter();
    }

}
