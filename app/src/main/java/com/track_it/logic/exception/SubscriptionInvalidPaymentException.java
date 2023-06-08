package com.track_it.logic.exception;

public class SubscriptionInvalidPaymentException extends SubscriptionException
{

    public SubscriptionInvalidPaymentException(String error) {
        super( error);
    }
}
