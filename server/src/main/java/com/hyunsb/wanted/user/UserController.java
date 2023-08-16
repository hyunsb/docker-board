package com.hyunsb.wanted.user;

import com.hyunsb.wanted._core.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<Object> Signup(
            @RequestBody @Valid UserRequest.SignupDTO signupDTO, Errors errors) {
        log.info("POST /user : " + signupDTO);

        userService.signup(signupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(
            @RequestBody @Valid UserRequest.SigninDTO signinDTO, Errors errors) {
        log.info("POST /signin : " + signinDTO);

        String jwt = userService.signin(signinDTO);
        return ResponseEntity.ok().header(JwtProvider.HEADER, jwt).build();
    }
}
