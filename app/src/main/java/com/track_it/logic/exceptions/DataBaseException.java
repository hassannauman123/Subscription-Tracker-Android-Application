package com.track_it.logic.exceptions;

public class DataBaseException extends RuntimeException
{
    public DataBaseException(String error) {
        super(error);
    }
}
