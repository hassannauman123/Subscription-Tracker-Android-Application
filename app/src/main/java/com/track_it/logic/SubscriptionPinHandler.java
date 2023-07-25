package com.track_it.logic;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.persistence.fakes.FakeSubscriptionPinPersistenceDB;

import java.util.List;

public class SubscriptionPinHandler {


    //3 functions add/remove/getAllPinnedSubObj

    private FakeSubscriptionPinPersistenceDB pinnedSubfakeDB = new FakeSubscriptionPinPersistenceDB();


    public void addPinnedSub(SubscriptionObj pinnedSub){
        pinnedSubfakeDB.addPinToDB(pinnedSub);
    }


    public void removePinnedSub(SubscriptionObj pinnedSub){
        if(pinnedSub!=null)
            pinnedSubfakeDB.removePinByID(pinnedSub.getID());
    }

    public List<SubscriptionObj> getAllPinnedSub(){
        return pinnedSubfakeDB.getAllPinnedSubscriptions();
    }

}
