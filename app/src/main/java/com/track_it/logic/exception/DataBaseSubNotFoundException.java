package com.track_it.logic.exception;


public class DataBaseSubNotFoundException extends DataBaseException
{


    public DataBaseSubNotFoundException() {
        super("Subscription not found in dataBase!");
    }
    public DataBaseSubNotFoundException(String error) {
        super(error);
    }

}
