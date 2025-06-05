package com.frotahucp.gestaofrotas.exception;

public class ViaCepException extends RuntimeException {
    public ViaCepException(String message) {
        super(message);
    }

    public ViaCepException(String message, Throwable cause) {
        super(message, cause);
    }
}