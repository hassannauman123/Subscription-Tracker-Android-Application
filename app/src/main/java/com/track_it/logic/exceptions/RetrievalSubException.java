package com.track_it.logic.exceptions;


public class RetrievalSubException extends RetrievalException
{


    public RetrievalSubException() // The Default message passed when using null constructor
    {
        super("Subscription could not be retrieved!");
    }
    public RetrievalSubException(String error)  // Custom message passed
    {
        super(error);
    }

}
