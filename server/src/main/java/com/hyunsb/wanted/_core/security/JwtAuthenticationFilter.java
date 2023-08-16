package com.hyunsb.wanted._core.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hyunsb.wanted._core.error.ErrorMessage;
import com.hyunsb.wanted._core.util.FilterResponse;
import com.hyunsb.wanted.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(JwtProvider.HEADER);
        log.info("JWT 필터 탐");

        if (token == null) {
            log.info("JWT 토큰 없음");
            chain.doFilter(request, response);
            return;
        }

        try {
            DecodedJWT decodedJWT = jwtProvider.verify(token);
            Long userId = getUserIdFromToken(decodedJWT);
            String role = decodedJWT.getClaim("role").asString();

            request.setAttribute(JwtProvider.REQUEST, userId);

            User user = User.builder().id(userId).role(role).build();

            PrincipalUserDetail myUserDetails = new PrincipalUserDetail(user);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            myUserDetails,
                            myUserDetails.getPassword(),
                            myUserDetails.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);

        } catch (SignatureVerificationException sve) {
            FilterResponse.unAuthorized(response, ErrorMessage.TOKEN_UN_AUTHORIZED);
        } catch (TokenExpiredException tee) {
            FilterResponse.unAuthorized(response, ErrorMessage.TOKEN_EXPIRED);
        } catch (JWTDecodeException exception) {
            FilterResponse.unAuthorized(response, ErrorMessage.TOKEN_VERIFICATION_FAILED);
        }
    }

    private Long getUserIdFromToken(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim(JwtProvider.REQUEST).asLong();
    }
}
