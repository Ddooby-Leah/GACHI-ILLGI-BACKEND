package com.ddooby.gachiillgi.base.enums.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthErrorCodeEnum implements ErrorCodeEnum {
    INVALID_CREDENTIALS("Invalid credentials", "Invalid email or password"),
    PASSWORD_MISMATCH("Password mismatch", "Passwords do not match"),
    INVALID_TOKEN("This token is unsupported", "Invalid token"),
    INVALID_TOKEN_SIGNATURE("잘못된 JWT 서명입니다.", "Invalid token"),
    EXPIRED_TOKEN("Token has expired", "Invalid token"),
    MISSING_AUTH_HEADER("Missing authorization header", "Invalid token"),
    UNAUTHORIZED_ACCESS("Unauthorized access to the requested resource", "Unauthorized access"),
    ACCESS_DENIED("Access to the requested resource is denied", "Access denied"),
    MUST_MAIL_VERIFICATION("메일인증을 수행해 주세요.", "메일인증"),
    MAIL_SEND_ERROR("메일 전송 실패", "메일인증"),
    INVALID_MAIL_LINK("유효하지 않은 인증메일 링크입니다.", "메일인증"),
    EXPIRED_MAIL_LINK("메일인증기한이 지났습니다. 메일인증 링크를 재발급 해주세요.", "메일인증"),
    ALREADY_COMPLETE_VERIFICATION("이미 메일인증에 완료되셨습니다.", "메일인증"),
    ALREADY_COMPLETE_ADD_DETAIL("이미 추가정보 입력이 완료되셨습니다.", "메일인증"),
    KAKAO_AUTH_CANCEL("카카오 인증 과정 중 취소처리 되었습니다.", "카카오 인증"),
    KAKAO_AUTH_GET_TOKEN_ERROR("카카오 인증 토큰을 얻어오는 중에 에러가 발생하였습니다.", "카카오 인증"),
    KAKAO_AUTH_GET_USERINFO_ERROR("카카오 유저 정보를 얻어오는 중에 에러가 발생하였습니다.", "카카오 인증"),

    ;

    private final String longMessage;
    private final String shortMessage;

    public static AuthErrorCodeEnum findByType(String type) {
        try {
            return AuthErrorCodeEnum.valueOf(type);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }

    @Override
    public String getLongMessage() {
        return longMessage;
    }

    @Override
    public String getShortMessage() {
        return shortMessage;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
