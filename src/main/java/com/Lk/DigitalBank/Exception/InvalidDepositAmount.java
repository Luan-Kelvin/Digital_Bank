package com.Lk.DigitalBank.Exception;

public class InvalidDepositAmount extends RuntimeException {
    public InvalidDepositAmount(String message) {
        super(message);
    }
}
