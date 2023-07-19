package com.track_it.logic.exceptions;


public class DatabaseSubNotFoundException extends DatabaseException
{


    public DatabaseSubNotFoundException() // The Default message passed when using null constructor
    {
        super("Subscription not found in database!");
    }
    public DatabaseSubNotFoundException(String error)  // Custom message passed
    {
        super(error);
    }

}
