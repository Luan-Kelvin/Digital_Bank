package com.Lk.DigitalBank.Exception;

public class PixTransferFailedException extends RuntimeException {
    public PixTransferFailedException(String message) {
        super(message);
    }
}
