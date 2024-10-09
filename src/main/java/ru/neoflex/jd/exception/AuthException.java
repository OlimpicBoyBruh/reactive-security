package ru.neoflex.jd.exception;

public class AuthException extends ApiException{

    public AuthException(String message, String errorCode) {
        super(message, errorCode);
    }
}
