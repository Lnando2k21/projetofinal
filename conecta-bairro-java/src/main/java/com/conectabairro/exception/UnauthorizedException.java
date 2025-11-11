package com.conectabairro.exception;

/**
 * Exceção lançada quando acesso não autorizado é tentado
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
