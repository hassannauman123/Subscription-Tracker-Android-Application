package com.track_it.exception;

public class DataBaseFullException extends RuntimeException
{
    public DataBaseFullException(String error) {
        super("Subscription DataBase is full:\n" + error);
    }

}

