package com.track_it.logic.exception;


public class DataBaseSubNotFoundException extends DataBaseException
{


    public DataBaseSubNotFoundException() // The Default message passed when using null constructor
    {
        super("Subscription not found in dataBase!");
    }
    public DataBaseSubNotFoundException(String error)  // Custom message passed
    {
        super(error);
    }

}
