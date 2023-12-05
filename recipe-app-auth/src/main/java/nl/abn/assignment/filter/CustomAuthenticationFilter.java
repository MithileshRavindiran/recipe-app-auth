/*
 * Copyright (c) mithilesh89ece@gmail.com. All Rights Reserved.
 * ============================================================
 */
package nl.abn.assignment.filter;

import static nl.abn.assignment.filter.ClaimConstants.CLAIM_ROLES;
import static nl.abn.assignment.filter.ClaimConstants.CLAIM_UID;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import nl.abn.assignment.service.TokenParserService;

/**
 * Authentication filter
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private TokenParserService tokenParserService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, TokenParserService tokenParserService) {
        setAuthenticationManager(authenticationManager);
        this.tokenParserService = tokenParserService;
    }

    /**
     * Validate user login to generate the JWT token
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        Authentication authentication = getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
        return authentication;
    }

    /**
     * Token generation if the authentication was successful
     *
     * @param request
     * @param response
     * @param chain
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        tokenParserService.addJWTToken(response, authentication, request);
    }
}
