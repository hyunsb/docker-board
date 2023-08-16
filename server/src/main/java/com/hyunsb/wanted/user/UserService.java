package com.hyunsb.wanted.user;

import com.hyunsb.wanted._core.error.ErrorMessage;
import com.hyunsb.wanted._core.error.exception.UserNotFoundException;
import com.hyunsb.wanted._core.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(UserRequest.SignupDTO signupDTO) {
        User signupUser = signupDTO.toEntityWithEncodedPassword(passwordEncoder);
        userRepository.save(signupUser);
    }

    @Transactional(readOnly = true)
    public String signin(UserRequest.SigninDTO signinDTO) {
        final String email = signinDTO.getEmail();
        final String rawPassword = signinDTO.getPassword();

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_NOT_FOUND));
        if (!user.isCorrectPassword(passwordEncoder, rawPassword))
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);

        return jwtProvider.create(user);
    }
}
