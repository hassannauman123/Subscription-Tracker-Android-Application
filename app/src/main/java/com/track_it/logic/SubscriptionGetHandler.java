package com.track_it.logic;

import com.track_it.domainObject.SubscriptionObj;
import com.track_it.exception.DataBaseException;
import com.track_it.persistence.DataBase;

public class SubscriptionGetHandler extends SubscriptionHandler
{


    public SubscriptionObj getSubscriptionByID(int inputID) throws DataBaseException
    {
        DataBase DBHandler = new DataBase();
        SubscriptionObj returnSub  =  DBHandler.getSubscriptionByID(inputID);
        return returnSub;

    }
}
