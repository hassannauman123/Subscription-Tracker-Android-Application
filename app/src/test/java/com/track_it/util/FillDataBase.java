package com.track_it.util;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;

import java.util.List;


public class FillDataBase
{


    public static void fillDataBaseRandomSubscriptions(final SubscriptionHandler subHandler, int numSubs) {

        try {
            for (int i = 0; i < numSubs; i++) {


                String subName = getSubName(subHandler);

                String frequency = getFrequency(subHandler);

                int payment = (int) ( Math.random() * subHandler.getMaxPaymentCentsTotal()) + 1;


                SubscriptionObj newSub = new SubscriptionObj(subName, payment, frequency);

                subHandler.addSubscription(newSub);

            }
        }
        catch (Exception e )
        {
            System.out.println("Something went wrong with creating random data: " + e.getMessage());
            assert(false); // Just crash
        }

    }

    private static String getFrequency(final SubscriptionHandler subHandler)
    {
        String frequencyToReturn = "";
        List <String> AllowableFrequencies = subHandler.getFrequencyNameList();

        int randFrequency = (int) ( Math.random() * AllowableFrequencies.size());

        frequencyToReturn = AllowableFrequencies.get(randFrequency);

        return frequencyToReturn;
    }

    private static String getSubName (final SubscriptionHandler subHandler)
    {
        String nameToReturn = "";
        String validChars = subHandler.getAllowableChars();


        for (int i =0 ;i <subHandler.getMaxNameLength(); i++)
        {
            int randChar = (int) (Math.random() * validChars.length());

            nameToReturn +=  validChars.charAt(randChar);

        }

        nameToReturn = nameToReturn.trim();

        return nameToReturn;

    }




    //Fill database with fake data that is less random, and looks more normal
    public static void fillFakeData(final SubscriptionHandler subHandler)
    {

        // Create 10 subs, with less random Names
        List<String> FrequencyList = subHandler.getFrequencyNameList();
        int numFrequency = subHandler.getNumFrequencies();

        for (int i =0 ; i < 10; i++)
        {
            try {
                String inputName = "Rand name For subscription" + i;
                String frequency = FrequencyList.get(i % numFrequency);
                int payment = (int) (Math.random() * subHandler.getMaxPaymentCentsTotal() + 1);
                SubscriptionObj currSub = new SubscriptionObj(inputName, payment,frequency);
                subHandler.addSubscription(currSub);

            }
            catch (Exception e)
            {
                System.out.println("ERROR WITH MAKING FAKE DATA!!: " +   e.getMessage());
                assert(false); // Just make the app crash for now
            }
        }


        //Create one long name of max length
        try {

            int payment = 22444;
            String inputName = "long name ";
            for (int i = inputName.length(); i < subHandler.getMaxNameLength(); i++)
            {
                inputName = inputName + "1";
            }
            String frequency = FrequencyList.get(0);
            SubscriptionObj currSub = new SubscriptionObj(inputName, payment, frequency);
            subHandler.addSubscription(currSub);
        }
        catch (Exception e)
        {
            System.out.println("ERROR WITH MAKING FAKE DATA!!: " +   e.getMessage());
            assert(false); // Just make the app crash for now
        }
    }

}
