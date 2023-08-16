package com.hyunsb.wanted._core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hyunsb.wanted.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    public static final Long EXP = 1000L * 60 * 60;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    public static final String REQUEST = "userId";

    private static String key;

    private final Environment environment;

    @PostConstruct
    private void init() {
        key = environment.getProperty("JWT_SECRET_KEY");
    }

    public String create(User user) {
        String jwt = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .withClaim("role", user.getRole())
                .withClaim(REQUEST, user.getId())
                .sign(Algorithm.HMAC512(key));

        log.info("JWT created: authentication object is creation");
        return TOKEN_PREFIX + jwt;
    }

    public DecodedJWT verify(String jwt) throws SignatureVerificationException, TokenExpiredException {
        log.info("JWT verify: " + jwt);
        String origin = getOriginalJWT(jwt);
        return JWT.require(Algorithm.HMAC512(key))
                .build()
                .verify(origin);
    }

    private static String getOriginalJWT(String jwt) {
        return jwt.replace(JwtProvider.TOKEN_PREFIX, "");
    }
}
