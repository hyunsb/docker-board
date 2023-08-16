package com.hyunsb.wanted.user;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hyunsb.wanted._core.error.exception.UserNotFoundException;
import com.hyunsb.wanted._core.security.JwtProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Nested
    @DisplayName("사용자 회원가입 통합 테스트")
    class Signup {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            UserRequest.SignupDTO signupDTO =
                    UserRequest.SignupDTO.builder()
                            .email("user@example.com")
                            .password("12345678")
                            .build();

            // When
            // Then
            userService.signup(signupDTO);
        }
    }

    @Nested
    @DisplayName("사용자 로그인 통합 테스트")
    class Signip {

        @BeforeEach
        void setUp() {
            UserRequest.SignupDTO signupDTO =
                    UserRequest.SignupDTO.builder()
                            .email("user@example.com")
                            .password("12345678")
                            .build();

            userService.signup(signupDTO);
        }

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            UserRequest.SigninDTO signinDTO =
                    UserRequest.SigninDTO.builder()
                            .email("user@example.com")
                            .password("12345678")
                            .build();

            // When
            String actual = userService.signin(signinDTO);
            DecodedJWT decodedJWT = jwtProvider.verify(actual);

            // Then
            Assertions.assertTrue(actual.contains(JwtProvider.TOKEN_PREFIX));
        }

        @DisplayName("실패 - 유효하지 않은 이메일")
        @Test
        void failure_Test_InvalidEmail() {
            // Given
            UserRequest.SigninDTO signinDTO =
                    UserRequest.SigninDTO.builder()
                            .email("Invalid@Email.com")
                            .password("12345678")
                            .build();

            // When
            // Then
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.signin(signinDTO));
        }

        @DisplayName("실패 - 유효하지 않은 패스워드")
        @Test
        void failure_Test_InvalidPassword() {
            // Given
            UserRequest.SigninDTO signinDTO =
                    UserRequest.SigninDTO.builder()
                            .email("user@example.com")
                            .password("Invalid")
                            .build();

            // When
            // Then
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.signin(signinDTO));
        }
    }
}
