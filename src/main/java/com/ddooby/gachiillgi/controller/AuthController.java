package com.ddooby.gachiillgi.controller;

import com.ddooby.gachiillgi.base.jwt.TokenProvider;
import com.ddooby.gachiillgi.dto.LoginDTO;
import com.ddooby.gachiillgi.dto.TokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.ddooby.gachiillgi.base.enums.TokenEnum.TOKEN_COOKIE_HEADER;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @Value("${jwt.token-validity-in-seconds}")
    private String tokenExpireTime;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDTO> authorize(@Valid @RequestBody LoginDTO loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(
                HttpHeaders.SET_COOKIE,
                ResponseCookie.from(TOKEN_COOKIE_HEADER.name(), token)
                        .maxAge(Long.parseLong(tokenExpireTime))
                        .secure(true)
                        .httpOnly(true)
                        .path("/")
                        .build()
                        .toString()
        );

        return new ResponseEntity<>(new TokenDTO(token), httpHeaders, HttpStatus.OK);
    }
}
