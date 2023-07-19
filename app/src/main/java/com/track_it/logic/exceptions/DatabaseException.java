package com.track_it.logic.exceptions;

public class DatabaseException extends RuntimeException
{
    public DatabaseException(String error) {
        super(error);
    }
}
