package com.track_it.logic.exception;

public class DataBaseFullException extends RuntimeException
{
    public DataBaseFullException(String error) {
        super("Subscription DataBase is full:\n" + error);
    }

}

