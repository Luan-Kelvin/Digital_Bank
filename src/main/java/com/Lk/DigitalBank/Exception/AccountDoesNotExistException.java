package com.Lk.DigitalBank.Exception;

public class AccountDoesNotExistException extends RuntimeException {
    public AccountDoesNotExistException(String message) {
        super(message);
    }
}
