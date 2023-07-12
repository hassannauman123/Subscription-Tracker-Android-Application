package com.track_it.util;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;

import java.util.List;


//This class is used to fill a database (using a SubscriptionHandler) with fake data

public class FillDataBase {


    //Fill database with subscriptions that are random
    public static void fillDataBaseRandomSubscriptions(final SubscriptionHandler subHandler, int numSubs) {

        try {
            for (int i = 0; i < numSubs; i++) {


                String subName = getSubName(subHandler);

                String frequency = getFrequency(subHandler);

                int payment = (int) (Math.random() * subHandler.getMaxPaymentCentsTotal()) + 1;


                SubscriptionObj newSub = new SubscriptionObj(subName, payment, frequency);

                subHandler.addSubscription(newSub);

            }
        } catch (Exception e) {
            System.out.println("Something went wrong with creating random data: " + e.getMessage());
            assert (false); // Just crash the test
        }

    }

    private static String getFrequency(final SubscriptionHandler subHandler) {
        String frequencyToReturn = "";
        List<String> AllowableFrequencies = subHandler.getFrequencyNameList();

        int randFrequency = (int) (Math.random() * AllowableFrequencies.size());

        frequencyToReturn = AllowableFrequencies.get(randFrequency);

        return frequencyToReturn;
    }

    private static String getSubName(final SubscriptionHandler subHandler) {
        String nameToReturn = "";
        String validChars = subHandler.getAllowableChars();

        int randomNameLength = (int) (Math.random() * subHandler.getMaxNameLength()) + subHandler.getMinNameLength();


        for (int i = 0; i < randomNameLength; i++) {
            int randChar = (int) (Math.random() * validChars.length());

            nameToReturn += validChars.charAt(randChar);

        }

        nameToReturn = nameToReturn.trim(); // trim off the blank spaces


        // In case of the slight chance that trim made the string too small, keep adding chars until it's the correct size
        while (nameToReturn.length() < subHandler.getMinNameLength()) {
            int randChar = (int) (Math.random() * validChars.length());
            nameToReturn += validChars.charAt(randChar);
            nameToReturn = nameToReturn.trim(); // trim off the blank spaces
        }

        return nameToReturn;

    }


    //Fill database with fake data that is less random, and looks more normal
    public static void fillFakeData(final SubscriptionHandler subHandler) {

        // Create 10 subs, with less random Names
        List<String> FrequencyList = subHandler.getFrequencyNameList();
        int numFrequency = subHandler.getNumFrequencies();

        for (int i = 0; i < 10; i++) {
            try {
                String inputName = "Rand name For subscription" + i;
                String frequency = FrequencyList.get(i % numFrequency);
                int payment = (int) (Math.random() * subHandler.getMaxPaymentCentsTotal() + 1);
                SubscriptionObj currSub = new SubscriptionObj(inputName, payment, frequency);
                subHandler.addSubscription(currSub);

            } catch (Exception e) {
                System.out.println("ERROR WITH MAKING FAKE DATA!!: " + e.getMessage());
                assert (false); // Just make the test crash for now
            }
        }


        //Create one long name of max length
        try {

            int payment = 22444;
            String inputName = "long name ";
            for (int i = inputName.length(); i < subHandler.getMaxNameLength(); i++) {
                inputName = inputName + "1";
            }
            String frequency = FrequencyList.get(0);
            SubscriptionObj currSub = new SubscriptionObj(inputName, payment, frequency);
            subHandler.addSubscription(currSub);
        } catch (Exception e) {
            System.out.println("ERROR WITH MAKING FAKE DATA!!: " + e.getMessage());
            assert (false); // Just make the test crash for now
        }
    }


    //Fill database with fake data that is not random, using defined objects
    public static void fillDataBaseNormal(final SubscriptionHandler subHandler)
    {
        // Create 10 subs, with less random Names
        List<String> FrequencyList = subHandler.getFrequencyNameList();
        int numFrequency = subHandler.getNumFrequencies();

        try {

            String inputName = "Zoo Pass";
            String frequency = FrequencyList.get( 0);
            int payment = 1;
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

             inputName = "Frank ";
             frequency = FrequencyList.get(1);
             payment = 1;
             currSub = new SubscriptionObj(inputName, payment, frequency);
            subHandler.addSubscription(currSub);

             inputName = "Frank ";
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
