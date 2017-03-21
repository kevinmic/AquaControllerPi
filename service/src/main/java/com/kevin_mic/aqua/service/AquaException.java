package com.kevin_mic.aqua.service;

public class AquaException extends RuntimeException {
    public AquaException(ErrorType errorType) {
        super(errorType.name());
    }
}
