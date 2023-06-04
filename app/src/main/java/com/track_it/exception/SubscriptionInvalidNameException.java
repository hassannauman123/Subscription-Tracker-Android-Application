package com.track_it.exception;

public class SubscriptionInvalidNameException extends SubscriptionException
{
    public SubscriptionInvalidNameException(String error) {
        super( error);
    }
}
