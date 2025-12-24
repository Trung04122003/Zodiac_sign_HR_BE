// ============================================================
// File: exception/BadRequestException.java
// ============================================================
package com.jci.zodiac.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}