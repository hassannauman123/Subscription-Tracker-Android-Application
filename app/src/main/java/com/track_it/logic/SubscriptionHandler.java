package com.track_it.logic;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.exception.DataBaseException;
import com.track_it.logic.exception.SubscriptionException;
import com.track_it.logic.exception.SubscriptionInvalidNameException;
import com.track_it.logic.exception.SubscriptionInvalidPaymentException;
import com.track_it.persistence.DataBase;
import java.util.ArrayList;


//This class is used to handle and implement the logical manipulation of the subscription objects.
// It also sets the allowable parameters of a subscription object.

public class SubscriptionHandler {


    //Every variable here sets what are allowable values of a subscription object

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_PAYMENT_DOLLAR = 9999;
    private static final int MAX_PAYMENT_CENTS = 99;

    private static final int MAX_PAYMENT_CENTS_TOTAL = MAX_PAYMENT_DOLLAR * 100 + MAX_PAYMENT_CENTS; // Maximum payment will be 9999.99 or 999999 cents

    private DataBase dataBaseHandler; //DatabaseHandler

    public static enum FREQUENCY //Allowable frequencies,
    {
        weekly,
        monthly,
        yearly
    }


    private static int NUM_FREQUENCIES = 3;
    private static final String[] ALLOWABLE_FREQUENCIES = {FREQUENCY.weekly.toString() ,FREQUENCY.monthly.toString(), FREQUENCY.yearly.toString()};

    private static final String allowableCharactersInName = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 _+*^&%$#@!+=|}]'?<>'"; // Our current list of allowable characters in the name


    public SubscriptionHandler()
    {
         dataBaseHandler = new DataBase();
    }



    // This function will add subscriptionToAdd to database. It will first validate subscription, and then
    // try to add to database.
    // It will throw Exceptions if anything goes wrong (like invalid data), so caller should be prepared to catch them.
    public void addSubscription(SubscriptionObj subscriptionToAdd) throws DataBaseException, SubscriptionException {
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
    // Must be one of the allowable strings in ALLOWABLE_FREQUENCIES
    public void validateFrequency(String inputName) throws SubscriptionInvalidPaymentException {
        boolean match = false;

        if (inputName.equals("")) {
            throw new SubscriptionInvalidPaymentException("Payment frequency required");

        }

        for (int i = 0; i < ALLOWABLE_FREQUENCIES.length; i++) {
            if (inputName.equals(ALLOWABLE_FREQUENCIES[i])) {
                match = true;
                break;
            }
        }

        if (!match) {
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


    // Returns how many digits are allowable before decimal place for payment amount
    public static int getMaxPaymentDigitsBeforeDecimal() {
        int payment = MAX_PAYMENT_DOLLAR;
        int digitsBeforeDecimalCount = 0;

        while (payment != 0) {
            payment = payment / 10;
            digitsBeforeDecimalCount++;
        }

        return digitsBeforeDecimalCount;
    }


    //returns the maximum amount a payment can be in cents
    public static int getMaxPaymentCentsTotal()
    {
        return MAX_PAYMENT_CENTS_TOTAL;
    }


    //Returns Min length a subscription name has to be
    public static int getMinNameLength() {
        return MIN_NAME_LENGTH;
    }


    // Max length of subscription name
    public static int getMaxNameLength() {
        return MAX_NAME_LENGTH;
    }

    // Allowable chars in name
    public static String getAllowableChars() {
        return allowableCharactersInName;
    }

    // Returns a string array of allowable frequencies
    public static String[] getFrequencyList() {
        String[] returnAllowableFrequencies = new String[ALLOWABLE_FREQUENCIES.length];

        for (int i = 0; i < ALLOWABLE_FREQUENCIES.length; i++) {
            returnAllowableFrequencies[i] = ALLOWABLE_FREQUENCIES[i];
        }

        return returnAllowableFrequencies;
    }

    public static int getNumFrequencies()
    {
        return NUM_FREQUENCIES;
    }



}
