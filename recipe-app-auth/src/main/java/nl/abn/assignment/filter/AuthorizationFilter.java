/*
 * Copyright (c) mithilesh89ece@gmail.com. All Rights Reserved.
 * ============================================================
 */
package nl.abn.assignment.filter;

import static java.util.Arrays.stream;
import static nl.abn.assignment.filter.ClaimConstants.CLAIM_ROLES;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import nl.abn.assignment.context.UserContext;
import nl.abn.assignment.exception.InvalidJwtToken;

/**
 * Authorization filter
 */
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    @Value("${app.security.token.signingKey}")
    private String signingKey;

    @Value("${app.security.token.bearer.prefix}")
    private String bearerPrefix;

    @Autowired
    private UserContext userContext;

    /**
     * To validate the incoming request with the bearer token and to check if the user is allowed to access it or else block it!
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/console")) {
            filterChain.doFilter(request, response);
        }
        else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(bearerPrefix)) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(signingKey.getBytes());
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(token);
                    String userName = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim(CLAIM_ROLES).asArray(String.class);
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, token, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    userContext.setUserName(userName);
                    //request.setAttribute("userKey",userName);
                    filterChain.doFilter(request, response);

                }
                catch (Exception ex) {
                    log.error("Issue with the Bearer Token Passed : {}", ex.getMessage());
                    response.setHeader("error", ex.getMessage());
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", ex.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                    throw new InvalidJwtToken("Issue with the Bearer Token Passed" + ex.getMessage());
                }
            }
            else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
