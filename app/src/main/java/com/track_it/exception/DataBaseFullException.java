package com.track_it.exception;

public class DataBaseFullException extends DataBaseException
{
    public DataBaseFullException(String error)
    {
        super("Subscription DataBase is full: " + error);
    }

}

