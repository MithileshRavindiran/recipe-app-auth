package nl.abn.assignment.service;

import static java.util.stream.Collectors.*;
import static nl.abn.assignment.filter.ClaimConstants.CLAIM_ROLES;
import static nl.abn.assignment.filter.ClaimConstants.CLAIM_UID;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class TokenParserService {

    @Value("${app.security.token.signingKey}")
    private String signingKey;

    @Value("${app.security.token.bearer.prefix}")
    private String bearerPrefix;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    public static final long REFRESH_TOKEN_VALIDITY = 30 * 60 * 60;

    public DecodedJWT parseTokenFromAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Retrieve the principal (which should contain the token)
        String token = (String) authentication.getCredentials();

        // Parse the token to get claims
        Algorithm algorithm = Algorithm.HMAC256(signingKey.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public void addJWTToken(HttpServletResponse response, Authentication authentication, HttpServletRequest request) {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(signingKey.getBytes());
        String access_token = JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .withIssuer(request.getRequestURL().toString())
            .withClaim(CLAIM_ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList()))
            .withClaim(CLAIM_UID, user.getUsername())
            .sign(algorithm);

        response.addHeader("Authorization", bearerPrefix + " " + access_token);
    }
}
