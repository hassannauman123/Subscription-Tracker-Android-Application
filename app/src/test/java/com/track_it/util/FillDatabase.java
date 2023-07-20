package com.track_it.util;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;

import java.util.List;


//This class is used to fill a database (using a SubscriptionHandler) with fake data

public class FillDatabase {






    //Fill database with fake data that is not random, using defined objects
    public static void fillDataBase(final SubscriptionHandler subHandler)
    {
        // Create 10 subs, with less random Names
        List<String> FrequencyList = subHandler.getFrequencyNameList();
        int numFrequency = subHandler.getNumFrequencies();

        try {

            String inputName = "Zoo Pass";
            String frequency = FrequencyList.get( 0);
            int payment = 2;
            SubscriptionObj currSub = new SubscriptionObj(inputName, payment, frequency);
            subHandler.addSubscription(currSub);

             inputName = "Amazon primeo";
            frequency = FrequencyList.get(2);
            payment = 33;
            currSub = new SubscriptionObj(inputName, payment, frequency);
            subHandler.addSubscription(currSub);


             inputName = "Zoo Pass";
             frequency = FrequencyList.get(0);
             payment = 33;
             currSub = new SubscriptionObj(inputName, payment, frequency);
             subHandler.addSubscription(currSub);

             inputName = "Golf club";
             frequency = FrequencyList.get(1);
             payment = 1;
             currSub = new SubscriptionObj(inputName, payment, frequency);
             subHandler.addSubscription(currSub);


            inputName = "Museum pass For Kids";
            frequency = FrequencyList.get(2);
            payment = 4567;
            currSub = new SubscriptionObj(inputName, payment, frequency);
            subHandler.addSubscription(currSub);

             inputName = "Golf club";
             frequency = FrequencyList.get(3);
             payment = 99999;
             currSub = new SubscriptionObj(inputName, payment, frequency);
             subHandler.addSubscription(currSub);


        } catch (Exception e) {
            System.out.println("ERROR WITH MAKING FAKE DATA!!: " + e.getMessage());
            assert (false); // Just make the test crash for now
        }
    }


}
