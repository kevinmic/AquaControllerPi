package com.kevin_mic.aqua.service;

import lombok.Data;

@Data
public class AquaException extends RuntimeException {
    private final ErrorType errorType;

    public AquaException(ErrorType errorType) {
        super(errorType.name());
        this.errorType = errorType;
    }
}
