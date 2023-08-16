package com.hyunsb.wanted.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunsb.wanted._core.security.JwtAuthenticationFilter;
import com.hyunsb.wanted._core.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        })
class UserControllerTest {

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("사용자 회원가입 컨트롤러 단위 테스트")
    class Signup {

        @DisplayName("성공")
        @Test
        @WithMockUser
        public void success_Test() throws Exception {
            // Given
            String uri = "/user";
            String email = "user@example.com";
            String password = "12345678";

            UserRequest.SignupDTO signupDTO =
                    UserRequest.SignupDTO.builder()
                            .email(email)
                            .password(password)
                            .build();

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .content(objectMapper.writeValueAsString(signupDTO)))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }
    }

    @Nested
    @DisplayName("사용자 로그인 컨트롤러 단위 테스트")
    class Signin {

        @DisplayName("성공")
        @Test
        @WithMockUser
        public void success_Test() throws Exception {
            // Given
            String uri = "/signin";
            String email = "user@example.com";
            String password = "12345678";
            String token = "token";

            UserRequest.SigninDTO signinDTO =
                    UserRequest.SigninDTO.builder()
                            .email(email)
                            .password(password)
                            .build();

            Mockito.when(userService.signin(ArgumentMatchers.any(UserRequest.SigninDTO.class)))
                    .thenReturn(token);

            // When
            // Then
            mockMvc.perform(MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .content(objectMapper.writeValueAsString(signinDTO)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.header().exists(JwtProvider.HEADER))
                    .andExpect(MockMvcResultMatchers.header().string(JwtProvider.HEADER, token));
        }
    }
}