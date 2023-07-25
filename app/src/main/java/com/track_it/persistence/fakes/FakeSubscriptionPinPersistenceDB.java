package com.track_it.persistence.fakes;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.exceptions.RetrievalSubException;

import java.util.ArrayList;
import java.util.List;



public class FakeSubscriptionPinPersistenceDB {



    private static ArrayList<SubscriptionObj> pinDB = new ArrayList<SubscriptionObj>(); // Create a static ArrayList that hold Pins

    //array list of sub obj and anything in this array list is pinned
    //fnc to add/remove

    int numPins;



    public void addPinToDB(SubscriptionObj pinnedSub) {

            pinDB.add(pinnedSub);
            numPins++;

    }


    public void removePinByID(int subscriptionID) throws RetrievalSubException {

        boolean removed = false; // Were we able to remove the pinned sub

        for (int i = 0; i < pinDB.size(); i++)  //Simply iterate through the list of pinned subscriptions, and look for the one with the right id
        {
            if (pinDB.get(i).getID() == subscriptionID) {
                pinDB.remove(i);
                removed = true;
            }
        }

        if (!removed) {
            throw new RetrievalSubException("Cannot delete subscription, subscription not found!");
        }
    }


    public List<SubscriptionObj> getAllPinnedSubscriptions() {

        ArrayList<SubscriptionObj> returnListOfPinnedSubs = new ArrayList<SubscriptionObj>();

        //Go through the DataBase, and create fill the return arrayList with all the subscriptions in the database
        for (int i = 0; i < pinDB.size(); i++) {
            SubscriptionObj copyOfSubscription = pinDB.get(i).copy(); // Copy the sub so the calling function can't illegally modify our fake database
            returnListOfPinnedSubs.add(copyOfSubscription);

        }

        return returnListOfPinnedSubs;

    }










}
