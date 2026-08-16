package com.Lk.DigitalBank.Exception;

public class CardIsNotActiveForPurchasseException extends RuntimeException {
    public CardIsNotActiveForPurchasseException(String message) {
        super(message);
    }
}
