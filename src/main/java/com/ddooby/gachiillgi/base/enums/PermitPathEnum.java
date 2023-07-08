package com.ddooby.gachiillgi.base.enums;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public enum PermitPathEnum {
    MAIN_URI("/"),
    API_DOCS_URI("/api-docs/**"),
    SWAGGER_UI_URI("/swagger-ui/**"),
    ACTUATOR_URI("/actuator/**"),
    FAVICON_URI("/favicon.ico"),
    HELLO_URI("/api/user/hello"),
    REDIRECT_URI("/api/user/test-redirect"),
    AUTHENTICATE_URI("/api/auth/login"),
    SIGNUP_URI("/api/auth/signup"),
    MAIL_URI("/api/auth/mail");

    private final String uri;

    public static RequestMatcher[] getPermitUriList() {
        return Arrays.stream(PermitPathEnum.values())
                .map(x -> new AntPathRequestMatcher(x.uri))
                .toArray(RequestMatcher[]::new);
    }
}