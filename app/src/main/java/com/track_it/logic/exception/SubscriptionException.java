package com.track_it.logic.exception;


//This is the base class for a subscription exception. Other more specific exceptions can extend this class
public class SubscriptionException extends RuntimeException

{

    public SubscriptionException(String error)
    {

        super( error);
    }

}
