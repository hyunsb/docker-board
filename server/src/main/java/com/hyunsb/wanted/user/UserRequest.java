package com.hyunsb.wanted.user;

import com.hyunsb.wanted._core.error.ErrorMessage;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    @Getter
    public static class SignupDTO {

        @Email(regexp = ".*@.*", message = ErrorMessage.INVALID_EMAIL_FORMAT)
        private String email;

        @Size(min = 8, message = ErrorMessage.INVALID_PASSWORD_LENGTH)
        private String password;

        public User toEntityWithEncodedPassword(PasswordEncoder passwordEncoder) {
            return User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    @Getter
    public static class SigninDTO {

        @Email(regexp = ".*@.*", message = ErrorMessage.INVALID_EMAIL_FORMAT)
        private String email;

        @Size(min = 8, message = ErrorMessage.INVALID_PASSWORD_LENGTH)
        private String password;
    }
}
