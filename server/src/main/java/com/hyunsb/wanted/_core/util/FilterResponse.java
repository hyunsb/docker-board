package com.hyunsb.wanted._core.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterResponse {

    private static final ObjectMapper OBJECT_MAPPER;
    private static final String CONTENT_TYPE;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        CONTENT_TYPE = "application/json; charset=utf-8";
    }

    public static void unAuthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(CONTENT_TYPE);

        String responseBody = OBJECT_MAPPER.writeValueAsString(message);
        response.getWriter().println(responseBody);
    }

    public static void forbidden(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(CONTENT_TYPE);

        String responseBody = OBJECT_MAPPER.writeValueAsString(message);
        response.getWriter().println(responseBody);
    }
}