package com.ddooby.gachiillgi.interfaces.controller;

import com.ddooby.gachiillgi.base.jwt.TokenProvider;
import com.ddooby.gachiillgi.domain.service.MailService;
import com.ddooby.gachiillgi.interfaces.dto.LoginRequestDTO;
import com.ddooby.gachiillgi.interfaces.dto.MailSendDTO;
import com.ddooby.gachiillgi.interfaces.dto.TokenDTO;
import com.ddooby.gachiillgi.interfaces.dto.UserDTO;
import com.ddooby.gachiillgi.domain.service.UserService;
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

import java.util.HashMap;
import java.util.Map;

import static com.ddooby.gachiillgi.base.enums.TokenEnum.TOKEN_COOKIE_HEADER;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @Value("${jwt.token-validity-in-seconds}")
    private String tokenExpireTime;

    private final UserService userService;
    private final MailService mailService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/signup")
    public UserDTO signup(@Valid @RequestBody UserDTO userDto) {
        return userService.signup(userDto);
    }

    @PostMapping("/mail")
    public void sendVerificationMail(@RequestBody MailSendDTO mailSendDTO) {
        log.debug(mailSendDTO.toString());
        String subject = "[가치일기] 안녕하세요, " + mailSendDTO.getUsername() + "님! 메일인증";
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", mailSendDTO.getUsername());
        variables.put("data", "여기를 눌러주세요");
        variables.put("location", "https://google.com");
        mailService.send(subject, variables, mailSendDTO.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> authorize(@Valid @RequestBody LoginRequestDTO loginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(
                HttpHeaders.SET_COOKIE,
                ResponseCookie.from(TOKEN_COOKIE_HEADER.getName(), token)
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