package com.track_it.logic;
import com.track_it.domainObject.SubscriptionObj;
import com.track_it.exception.*;
import com.track_it.persistence.DataBase;

public class AddSubscriptionHandler extends SubscriptionHandler {


    // Temp dummy function
    public  void addSubscription(SubscriptionObj subscriptionToAdd) throws DataBaseException, SubscriptionException

    {

        validateName(subscriptionToAdd.getName());
        validateFrequency(subscriptionToAdd.getPaymentFrequency());
        validatePaymentAmount(subscriptionToAdd.getTotalPaymentInCents());

        DataBase dataBaseHandler = new DataBase();

        dataBaseHandler.addSubscriptionDataBase(subscriptionToAdd); // Added to dataBase!!

    }




}
