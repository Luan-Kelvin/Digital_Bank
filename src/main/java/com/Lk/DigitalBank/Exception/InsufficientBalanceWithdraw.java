package com.Lk.DigitalBank.Exception;

public class InsufficientBalanceWithdraw extends RuntimeException {
    public InsufficientBalanceWithdraw(String message) {
        super(message);
    }
}
