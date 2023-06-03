package com.track_it.logic;

import com.track_it.domainObject.SubscriptionObj;
import com.track_it.exception.SubscriptionInvalidNameException;
import com.track_it.exception.SubscriptionInvalidPaymentException;
import com.track_it.domainObject.SubscriptionObj;
import com.track_it.exception.*;

public class SubscriptionHandler {


    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 100;

    private static final int MAX_PAYMENT_DOLLAR = 9999;
    private static final int MAX_PAYMENT_CENTS = 99;

    private static final int MAX_PAYMENT_CENTS_TOTAL = MAX_PAYMENT_DOLLAR * 100 + MAX_PAYMENT_CENTS; // Maximum payment will be 9999.99

    private static final String[] ALLOWABLE_FREQUENCIES = {"weekly", "monthly", "yearly"};

    private static final String allowableCharactersInName = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 _+*^&%$#@!+=\\|}]'?/<>'"; // Our current list of allowable characters in the name


    public void validatePaymentAmount(int paymentAmount) throws SubscriptionInvalidPaymentException {
        if (paymentAmount <= 0) {
            throw new SubscriptionInvalidPaymentException("Payment amount is too small");

        }
        if (paymentAmount > MAX_PAYMENT_CENTS_TOTAL) {
            throw new SubscriptionInvalidPaymentException("Payment amount is too large, Maximum payment is $" + MAX_PAYMENT_DOLLAR + "." + MAX_PAYMENT_CENTS);

        }
    }


    public static int getMaxPaymentDigitsBeforeDecimal() {
        int payment = MAX_PAYMENT_DOLLAR;
        int digitsBeforeDecimalCount = 0;

        while ( payment != 0) {
            payment = payment / 10;
            digitsBeforeDecimalCount++;
        }

        return digitsBeforeDecimalCount;
    }

    public static int getMinNameLength() {
        return MIN_NAME_LENGTH;
    }

    public static int getMaxNameLength() {
        return MAX_NAME_LENGTH;
    }

    public static String getAllowableChars() {
        return allowableCharactersInName;
    }

    public static String[] getAllowableFrequency() {
        String[] returnAllowableFrequencies = new String[ALLOWABLE_FREQUENCIES.length];

        for (int i = 0; i < ALLOWABLE_FREQUENCIES.length; i++) {
            returnAllowableFrequencies[i] = ALLOWABLE_FREQUENCIES[i];

        }

        return returnAllowableFrequencies;
    }

    public void validateName(String inputName) throws SubscriptionInvalidNameException {

        boolean verified = true;

        //The name has to be a minimum length
        if (inputName.trim().length() < MIN_NAME_LENGTH) {
            throw new SubscriptionInvalidNameException("Name needs to be at least 3 characters long");
        } else  // Now we know string is greater than 3 chars
        {
            if (!Character.isLetter((inputName.charAt(0)))) {
                throw new SubscriptionInvalidNameException("First character in name must be a letter");
            }
        }

        if (verified) // If no error has yet been detected, check that the name is not too long
        {
            if (inputName.length() > MAX_NAME_LENGTH) {
                throw new SubscriptionInvalidNameException("Name is too Long");

            }
        }

        if (verified) // If no error has yet been detected, makes sure blank spaces are not the first or last letters
        {
            if (!inputName.equals(inputName.trim())) // Blank spaces as first or last char
            {
                verified = false;

                throw new SubscriptionInvalidNameException("First and last characters must not be spaces");
            }
        }


        //Iterate through whole string, checks for invalid characters
        for (int i = 0; i < inputName.length(); i++) {
            if (allowableCharactersInName.indexOf(inputName.charAt(i)) == -1) {
                throw new SubscriptionInvalidNameException("Invalid characters detected!");
            }
        }


    }


    public void  validateFrequency(String inputName) throws SubscriptionInvalidPaymentException {
        boolean match = false;

        if (inputName.equals("")) {
            throw new SubscriptionInvalidPaymentException("You must enter a payment Frequency!");

        }


        for (int i = 0; i < ALLOWABLE_FREQUENCIES.length; i++) {
            if (inputName.equals(ALLOWABLE_FREQUENCIES[i])) {
                match = true;
                break;
            }
        }


        if (!match) {
            throw new SubscriptionInvalidPaymentException("Incorrect frequency Entered");
        }
    }

    public boolean validatePayment(String inputName) {
        return true;
    }


}
