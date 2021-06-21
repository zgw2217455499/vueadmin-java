package com.zhang.common.exception;


import org.springframework.security.core.AuthenticationException;

public class CaptchaException extends AuthenticationException {
    public CaptchaException(String explanation) {
        super(explanation);
    }
}
