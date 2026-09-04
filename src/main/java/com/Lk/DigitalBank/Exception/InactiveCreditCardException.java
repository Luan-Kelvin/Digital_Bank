package com.Lk.DigitalBank.Exception;

public class InactiveCreditCardException extends RuntimeException {
    public InactiveCreditCardException(String message) {
        super(message);
    }
}
