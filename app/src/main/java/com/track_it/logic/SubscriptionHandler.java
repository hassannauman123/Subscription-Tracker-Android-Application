package com.track_it.logic;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.exception.DataBaseException;
import com.track_it.logic.exception.SubscriptionException;
import com.track_it.logic.exception.SubscriptionInvalidNameException;
import com.track_it.logic.exception.SubscriptionInvalidPaymentException;
import com.track_it.persistence.DataBase;
import java.util.ArrayList;
import java.util.List;


//This class is used to handle and implement the logical manipulation of the subscription objects.
// It also sets the allowable parameters of a subscription object.

public class SubscriptionHandler {


    //Every variable here sets what are allowable values of a subscription object (This are set by injection)

    private final int MIN_NAME_LENGTH;
    private final int MAX_NAME_LENGTH;
    private final int MAX_PAYMENT_DOLLAR;
    private final int MAX_PAYMENT_CENTS;
   private final int MAX_PAYMENT_CENTS_TOTAL ; // Maximum payment will be 9999.99 or 999999 cents
    private final String allowableCharactersInName; // Our current list of allowable characters in the name

    private ArrayList<Frequency> FrequencyList  = new ArrayList<>();
    private DataBase dataBaseHandler; //DatabaseHandler


    // Default null constructor
    public SubscriptionHandler()
    {

        MIN_NAME_LENGTH = 1;
        MAX_NAME_LENGTH = 30;
        MAX_PAYMENT_DOLLAR = 9999;
        MAX_PAYMENT_CENTS = 99;
        MAX_PAYMENT_CENTS_TOTAL = MAX_PAYMENT_DOLLAR * 100 + MAX_PAYMENT_CENTS;
        dataBaseHandler = new DataBase();
        allowableCharactersInName = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 _+*^&%$#@!+=|}]'?<>'";
        InitFrequency();
    }

    public SubscriptionHandler( int inputMinNameLen, int inputMaxNameLen, int inputMaxPayDollar,int inputMaxPayCents,String inputAllowableChars)
    {
        dataBaseHandler = new DataBase(); // Change later
        MIN_NAME_LENGTH = inputMinNameLen;
        MAX_NAME_LENGTH = inputMaxNameLen;
        MAX_PAYMENT_DOLLAR = inputMaxPayDollar;
        MAX_PAYMENT_CENTS = inputMaxPayCents;
        MAX_PAYMENT_CENTS_TOTAL = MAX_PAYMENT_DOLLAR * 100 + MAX_PAYMENT_CENTS;
        allowableCharactersInName = inputAllowableChars;
        InitFrequency();

    }


    private void InitFrequency()
    {
        FrequencyList.add(new WeeklyFrequency());
        FrequencyList.add(new MonthlyFrequency());
        FrequencyList.add(new YearlyFrequency());

    }

    // This function will add subscriptionToAdd to database. It will first validate subscription, and then
    // try to add to database.
    // It will throw Exceptions if anything goes wrong (like invalid data), so caller should be prepared to catch them.
    public void addSubscription(SubscriptionObj subscriptionToAdd) throws DataBaseException, SubscriptionException
    {
        validateWholeSubscription(subscriptionToAdd); // might throw exception
        dataBaseHandler.addSubscriptionDataBase(subscriptionToAdd); // Added to dataBase!!

    }

    // Validate the whole subscription.
    // Throws exception if object is invalid
    public void validateWholeSubscription(SubscriptionObj subscriptionToValidate) throws SubscriptionException
    {
        validateName(subscriptionToValidate.getName());
        validateFrequency(subscriptionToValidate.getPaymentFrequency());
        validatePaymentAmount(subscriptionToValidate.getTotalPaymentInCents());
    }


    // Validate the Frequency input string
    // Must be one of the allowable frequencies in FrequencyList
    public void validateFrequency(String inputName) throws SubscriptionInvalidPaymentException {
        boolean match = false; // A boolean to tell if inputName matches any of our payment Frequencies

        for (Frequency currFrequency : FrequencyList)
        {
            if (currFrequency.checkMatch(inputName))
            {
                match = true;
            }
        }
        if (!match) // If there was no match
        {
            throw new SubscriptionInvalidPaymentException(inputName + " is not a valid frequency");
        }

    }


    // Validate the inputName.
    // Throws an exception if string invalid
    // Current rules:
    //      No trailing white spaces before or after string!
    //      Must be a least MIN_NAME_LENGTH long
    //      Must be less than or equal to MAX_NAME_LENGTH characters long
    //      chars are restricted to certain characters (look at allowableCharactersInName)

    public void validateName(String inputName) throws SubscriptionInvalidNameException {

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

        //Iterate through whole string, checks for invalid characters
        for (int i = 0; i < inputName.length(); i++) {
            if (allowableCharactersInName.indexOf(inputName.charAt(i)) == -1) {
                throw new SubscriptionInvalidNameException(inputName.charAt(i) + " is not allowed");
            }
        }


    }

    //This validates the input Payment amount
    // Throw exceptions if invalid
    // Currently a valid payment amount > 0, and less than MAX_PAYMENT_CENTS_TOTAL
    public void validatePaymentAmount(int paymentAmount) throws SubscriptionInvalidPaymentException {
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

    // Gets all the subscriptions in the database
    public ArrayList<SubscriptionObj> getAllSubscriptions() throws DataBaseException
    {
        return dataBaseHandler.queryGetAllSubs();
    }


    // Removes a subscription by ID from the database.
    // Will throw an Exception if subscription could not be deleted from database
    public void removeSubscriptionByID(int subscriptionID) throws DataBaseException {

        dataBaseHandler.removeSubscriptionByID(subscriptionID); // Remove it
    }


    // Edit a whole subscription, and save those changes to the database
    // This throws Exceptions if new data is invalid, subscriptionID is invalid, or the subscription can't be edited.
    // Input Parameters:
    //        subscriptionID  - The id of the subscription to change
    //       subscriptionToEdit - The details that the subscription will be changed to.
    public void editWholeSubscription(int subscriptionID, SubscriptionObj subscriptionToEdit) throws DataBaseException, SubscriptionException {
        validateWholeSubscription(subscriptionToEdit); // Validate the subscription
        dataBaseHandler.editSubscriptionByID(subscriptionID, subscriptionToEdit); // ave the edits.

    }



    // This part essentially returns what are allowable parameters for a subscription object.
    // I am not sure to have this in the logic layer, or attach it to the domain object directly?
    // I think it makes more sense to have it in the logic layer, in case we ever have different
    // allowable parameters for different users.


    //returns the maximum amount a payment can be in cents
    public  int getMaxPaymentCentsTotal()
    {
        return MAX_PAYMENT_CENTS_TOTAL;
    }


    //Returns Min length a subscription name has to be
    public  int getMinNameLength() {
        return MIN_NAME_LENGTH;
    }


    // Max length of subscription name
    public  int getMaxNameLength() {
        return MAX_NAME_LENGTH;
    }

    // Allowable chars in name
    public  String getAllowableChars() {
        return allowableCharactersInName;
    }

    // Returns a string array of allowable frequencies
    public List<String> getFrequencyList() {
        List<String> returnAllowableFrequencies = new ArrayList<String> ();

        for ( Frequency currFrequency : FrequencyList )
        {
            returnAllowableFrequencies.add(currFrequency.getFrequencyName());
        }

        return returnAllowableFrequencies;
    }

    public int getNumFrequencies()
    {
        return FrequencyList.size();
    }

}




