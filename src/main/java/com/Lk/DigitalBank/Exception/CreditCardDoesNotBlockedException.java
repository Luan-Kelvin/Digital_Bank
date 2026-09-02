package com.Lk.DigitalBank.Exception;

public class CreditCardDoesNotBlockedException extends RuntimeException {
    public CreditCardDoesNotBlockedException(String message) {
        super(message);
    }
}
