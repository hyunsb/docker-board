package com.hyunsb.wanted._core.security;

import com.hyunsb.wanted._core.error.ErrorMessage;
import com.hyunsb.wanted._core.util.FilterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final Environment environment;

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(environment);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .headers().frameOptions().sameOrigin()

                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().formLogin().disable()

                .httpBasic().disable()

                .apply(new SecurityFilterManager(jwtProvider()))

                .and().exceptionHandling().authenticationEntryPoint((request, response, authException) ->
                        FilterResponse.unAuthorized(response, ErrorMessage.TOKEN_UN_AUTHORIZED))

                .and().authorizeRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistry
                                .antMatchers(HttpMethod.POST, "/board").authenticated()
                                .antMatchers(HttpMethod.PUT, "/board/**").authenticated()
                                .antMatchers(HttpMethod.DELETE, "/board/**").authenticated()
                                .antMatchers("/board/**").permitAll()
                                .anyRequest().permitAll());

        return http.build();
    }

    @RequiredArgsConstructor
    public static class SecurityFilterManager extends AbstractHttpConfigurer<SecurityFilterManager, HttpSecurity> {

        private final JwtProvider jwtProvider;

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider));
            super.configure(builder);
        }
    }
}
