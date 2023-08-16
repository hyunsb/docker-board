package com.hyunsb.wanted._core.error;

public class ErrorMessage {

    public static final String INVALID_EMAIL_FORMAT = "유효하지 않은 이메일 형식입니다.";
    public static final String INVALID_PASSWORD_LENGTH = "비밀번호는 8자 이상이어야 합니다.";
    public static final String USER_NOT_FOUND = "아이디와 비밀번호를 다시 확인해주세요.";
    public static final String INVALID_BOARD_CONSTRUCTOR = "본인의 게시글만 수정 및 삭제가 가능합니다.";

    public static final String TOKEN_UN_AUTHORIZED = "토큰 인증에 실패하였습니다.";
    public static final String TOKEN_EXPIRED = "토큰이 만료되었습니다.";
    public static final String TOKEN_VERIFICATION_FAILED = "올바른 토큰이 아닙니다.";

    public static final String INVALID_USER = "유효하지 않은 회원 정보입니다.";
    public static final String EXCEEDED_SIZE = "게시글 수 요청의 최대 허용 범위를 초과했습니다.";
    public static final String INVALID_BOARD = "해당 아이디에 해당하는 게시글이 존재하지 않습니다.";
}
