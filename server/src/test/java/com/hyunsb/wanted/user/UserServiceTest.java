package com.hyunsb.wanted.user;

import com.hyunsb.wanted._core.error.exception.UserNotFoundException;
import com.hyunsb.wanted._core.security.JwtProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("사용자 회원가입 서비스 단위 테스트")
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

            Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                    .then(invocation -> invocation.getArgument(0));

            Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString()))
                    .then(invocation -> {
                        String rawPassword = invocation.getArgument(0);
                        return "encoded_" + rawPassword;
                    });

            // When
            // Then
            userService.signup(signupDTO);
        }
    }

    @Nested
    @DisplayName("사용자 로그인 서비스 단위 테스트")
    class Signin {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            UserRequest.SigninDTO signinDTO = UserRequest.SigninDTO.builder()
                    .email("user@example.com")
                    .password("password")
                    .build();

            User user = User.builder()
                    .id(1L)
                    .email("user@example.com")
                    .password("encoded_password")
                    .build();

            Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.of(user));

            Mockito.when(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                    .thenReturn(true);

            Mockito.when(jwtProvider.create(ArgumentMatchers.any(User.class)))
                    .then(invocation -> {
                        User response = invocation.getArgument(0);
                        return JwtProvider.TOKEN_PREFIX + response.getEmail();
                    });

            // When
            String jwt = userService.signin(signinDTO);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertTrue(jwt.contains(JwtProvider.TOKEN_PREFIX)),
                    () -> Assertions.assertTrue(jwt.contains(user.getEmail()))
            );
        }

        @DisplayName("실패 - 유효하지 않은 이메일")
        @Test
        void failure_Test_InvalidEmail() {
            // Given
            UserRequest.SigninDTO signinDTO = UserRequest.SigninDTO.builder()
                    .email("user@example.com")
                    .password("password")
                    .build();

            Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.empty());

            // When
            // Then
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.signin(signinDTO));
        }

        @DisplayName("실패 - 유효하지 않은 패스워드")
        @Test
        void failure_Test_InvalidPassword() {
            // Given
            UserRequest.SigninDTO signinDTO = UserRequest.SigninDTO.builder()
                    .email("user@example.com")
                    .password("password")
                    .build();

            User user = User.builder()
                    .id(1L)
                    .email("user@example.com")
                    .password("encoded_password")
                    .build();

            Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.of(user));

            Mockito.when(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                    .thenReturn(false);

            // When
            // Then
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.signin(signinDTO));
        }
    }
}