package com.track_it.exception;

public class DataBaseException extends RuntimeException
{

    public DataBaseException(String error) {
        super(error);
    }
}
