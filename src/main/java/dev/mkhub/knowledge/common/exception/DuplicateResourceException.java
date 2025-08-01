package dev.mkhub.knowledge.common.exception;

/**
 * 회원가입시 이메일이나 닉네임 중복발생 했을 시 정의하는 커스텀 Exception
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
