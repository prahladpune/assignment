package com.db.trade.exception;

public class InvalidTradeException extends RuntimeException {

    public InvalidTradeException(String message) {
        super(message);
    }

    public InvalidTradeException(Throwable throwable) {
        super(throwable);
    }

    public InvalidTradeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
