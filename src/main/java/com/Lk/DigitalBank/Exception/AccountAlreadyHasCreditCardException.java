package com.Lk.DigitalBank.Exception;

public class AccountAlreadyHasCreditCardException extends RuntimeException {
    public AccountAlreadyHasCreditCardException(String message) {
        super(message);
    }
}
