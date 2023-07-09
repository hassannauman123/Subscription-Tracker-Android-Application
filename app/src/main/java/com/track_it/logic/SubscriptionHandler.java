package com.track_it.logic;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.exceptions.DataBaseException;
import com.track_it.logic.exceptions.SubscriptionException;
import com.track_it.logic.exceptions.SubscriptionInvalidFrequencyException;
import com.track_it.logic.exceptions.SubscriptionInvalidNameException;
import com.track_it.logic.exceptions.SubscriptionInvalidPaymentException;
import com.track_it.logic.frequencies.*;
import com.track_it.persistence.SubscriptionPersistence;

import java.util.ArrayList;
import java.util.List;


//This class is used to handle and implement the logical manipulation of the subscription objects.=
// It also sets the allowable parameters of a subscription object.

public class SubscriptionHandler {


    //Every variable here sets what are allowable values of a subscription object (These are set by injection)
    private final int MIN_NAME_LENGTH; // Min length of name
    private final int MAX_NAME_LENGTH; // max length of name
    private final int MAX_PAYMENT_DOLLAR; // max payment amount
    private final int MAX_PAYMENT_CENTS; // max payment amount
    private final int MAX_PAYMENT_CENTS_TOTAL ;
    private final String allowableCharactersInName;

    private ArrayList<Frequency> frequencyList  = new ArrayList<>();
    private SubscriptionPersistence dataBaseHandler; //Database Handler



    public SubscriptionHandler( int inputMinNameLen, int inputMaxNameLen, int inputMaxPayDollar,int inputMaxPayCents,String inputAllowableChars, SubscriptionPersistence inputDB )
    {
        //Set the Data base, and various parameters for what is a valid subscription
        dataBaseHandler =  inputDB;
        MIN_NAME_LENGTH = inputMinNameLen;
        MAX_NAME_LENGTH = inputMaxNameLen;
        MAX_PAYMENT_DOLLAR = inputMaxPayDollar;
        MAX_PAYMENT_CENTS = inputMaxPayCents;
        MAX_PAYMENT_CENTS_TOTAL = MAX_PAYMENT_DOLLAR * 100 + MAX_PAYMENT_CENTS;
        allowableCharactersInName = inputAllowableChars;
        InitFrequency();
    }


    //Initialize the frequency list
    private void InitFrequency()
    {
        frequencyList.add(new DailyFrequency());
        frequencyList.add(new WeeklyFrequency());
        frequencyList.add(new MonthlyFrequency());
        frequencyList.add(new YearlyFrequency());
    }

    // This function will add subscriptionToAdd to database. It will first validate subscription, and then
    // try to add to database.
    // It will throw Exceptions if anything goes wrong (like invalid data), so caller should be prepared to catch them.
    public void addSubscription(SubscriptionObj subscriptionToAdd) throws DataBaseException, SubscriptionException
    {
        validateWholeSubscription(subscriptionToAdd); // May throw exception if subscription details are not valid
        dataBaseHandler.addSubscriptionToDB(subscriptionToAdd); // Add to dataBase, will throw DataBaseException if subscription could not be added to dat base.
    }



    //Return a frequency object that matches the frequency type of inputSubscription.
    // will throw SubscriptionInvalidFrequencyException if frequency of inputSubscription is not valid
    public Frequency getFrequencyObject( SubscriptionObj inputSubscription) throws SubscriptionInvalidFrequencyException
    {
        Frequency returnFrequency = null;

        for ( Frequency currFrequency : frequencyList) // Get a frequency object that matches the frequency of the inputSubscription
        {
            if (currFrequency.checkMatch(inputSubscription.getPaymentFrequency()))
            {
                returnFrequency = currFrequency; //It's Fine to return a non-copied object
            }
        }

        if (returnFrequency == null) // If no matching frequency was found, throw exception
        {
            throw new SubscriptionInvalidFrequencyException(inputSubscription.getPaymentFrequency() + " is not a valid frequency");
        }


        return returnFrequency; // return frequency

    }

    // Validate the whole subscription.
    // Throws exception if object is invalid
    public void validateWholeSubscription(final SubscriptionObj subscriptionToValidate) throws SubscriptionException
    {
        validateName(subscriptionToValidate.getName());
        validateFrequency(subscriptionToValidate.getPaymentFrequency());
        validatePaymentAmount(subscriptionToValidate.getTotalPaymentInCents());
    }


    // Validate the Frequency input string
    // Must be one of the allowable frequencies in FrequencyList
    public void validateFrequency(final String inputName) throws SubscriptionInvalidFrequencyException {
        boolean match = false; // A boolean to tell if inputName matches any of our payment Frequencies

        for (Frequency currFrequency : frequencyList)
        {
            if (currFrequency.checkMatch(inputName))
            {
                match = true;
            }
        }
        if (!match) // If there was no match
        {
            throw new SubscriptionInvalidFrequencyException(inputName + " is not a valid frequency");
        }

    }


    // Validate the inputName.
    // Throws an exception if string invalid
    // Current rules:
    //      No trailing white spaces before or after string!
    //      Must be a least MIN_NAME_LENGTH long
    //      Must be less than or equal to MAX_NAME_LENGTH characters long
    //      chars are restricted to certain characters (look at allowableCharactersInName)

    public void validateName(final String inputName) throws SubscriptionInvalidNameException {

        //The name has to be a minimum length
        if (inputName.trim().length() < MIN_NAME_LENGTH) {
            throw new SubscriptionInvalidNameException("Name required");
        }

         if (inputName.length() > MAX_NAME_LENGTH) {
            throw new SubscriptionInvalidNameException("Name is too long");
        }


        if (!inputName.equals(inputName.trim())) // Blank spaces as first or last char
        {

            throw new SubscriptionInvalidNameException("Must not start or end with spaces");
        }

        //Iterate through whole string, and check for invalid characters
        for (int i = 0; i < inputName.length(); i++) {
            if (allowableCharactersInName.indexOf(inputName.charAt(i)) == -1) {
                throw new SubscriptionInvalidNameException(inputName.charAt(i) + " is not an allowable\nchar in name");
            }
        }


    }

    // This validates the input Payment amount
    // Throw exceptions if invalid.
    // Currently a valid payment amount > 0, and less than MAX_PAYMENT_CENTS_TOTAL
    public void validatePaymentAmount(final int paymentAmount) throws SubscriptionInvalidPaymentException {
        if (paymentAmount <= 0) {
            throw new SubscriptionInvalidPaymentException("Payment amount is too small");
        }
        if (paymentAmount > MAX_PAYMENT_CENTS_TOTAL) {
            throw new SubscriptionInvalidPaymentException("Payment amount is too large. Maximum payment is $" + MAX_PAYMENT_DOLLAR + "." + MAX_PAYMENT_CENTS);

        }
    }


    // Gets and returns a single subscription by ID from the database.
    public SubscriptionObj getSubscriptionByID(int inputID) throws DataBaseException {

        SubscriptionObj returnSub = dataBaseHandler.getSubscriptionByID(inputID);
        return returnSub;

    }

    // Gets and returns list of all the subscriptions in the database
    public List<SubscriptionObj> getAllSubscriptions() throws DataBaseException
    {
        return dataBaseHandler.getAllSubscriptions();
    }


    // Removes a subscription by ID from the database.
    // Will throw an Exception if subscription could not be deleted from database
    public void removeSubscriptionByID(int subscriptionID) throws DataBaseException {

        dataBaseHandler.removeSubscriptionByID(subscriptionID); // Remove it
    }


    // Edit a whole subscription, and save those changes to the database.
    // This throws Exceptions if new data is invalid, subscriptionID is invalid, or the subscription can't be edited.
    // Input Parameters:
    //       subscriptionID  - The id of the subscription to change
    //       subscriptionToEdit - The details that the subscription will be changed to.
    public void editWholeSubscription(int subscriptionID,final SubscriptionObj subscriptionToEdit) throws DataBaseException, SubscriptionException {
        validateWholeSubscription(subscriptionToEdit); // Validate the subscription
        dataBaseHandler.editSubscriptionByID(subscriptionID, subscriptionToEdit); // ave the edits.
    }


    //returns the maximum amount a payment can be in cents
    public  int getMaxPaymentCentsTotal()
    {
        return MAX_PAYMENT_CENTS_TOTAL;
    }


    //returns the maximum amount a payment can be in dollars
    public  int getMaxPaymentDollarsTotal()
    {
        return MAX_PAYMENT_DOLLAR;
    }



    //Returns Min length a subscription name has to be
    public  int getMinNameLength() {
        return MIN_NAME_LENGTH;
    }


    // Returns the Max length a subscription name can be
    public  int getMaxNameLength() {
        return MAX_NAME_LENGTH;
    }

    // Returns the string of Allowable chars in name
    public  String getAllowableChars()
    {
        return allowableCharactersInName;
    }

    // Returns a string list of allowable frequencies in order
    public List<String> getFrequencyNameList() {
        List<String> returnAllowableFrequencies = new ArrayList<String> ();

        for ( Frequency currFrequency : frequencyList )
        {
            returnAllowableFrequencies.add(currFrequency.getFrequencyName());
        }

        return returnAllowableFrequencies;
    }

    // Return the number of frequencies
    public int getNumFrequencies()
    {
        return frequencyList.size();
    }

}




