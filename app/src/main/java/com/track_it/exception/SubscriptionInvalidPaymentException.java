package com.track_it.exception;

public class SubscriptionInvalidPaymentException extends SubscriptionException
{

    public SubscriptionInvalidPaymentException(String error) {
        super( error);
    }
}
