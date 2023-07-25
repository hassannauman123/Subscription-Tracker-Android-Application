package com.track_it.logic;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.logic.exceptions.SubscriptionException;
import com.track_it.logic.exceptions.SubscriptionInvalidFrequencyException;
import com.track_it.logic.exceptions.SubscriptionInvalidNameException;
import com.track_it.logic.exceptions.SubscriptionInvalidPaymentException;
import com.track_it.logic.exceptions.SubscriptionTagException;
import com.track_it.logic.frequencies.Frequency;

import java.util.List;

public class SubscriptionValidator {

    //Every variable here sets what are allowable values of a subscription object (These are set by injection)
    private final int MIN_NAME_LENGTH; // Min length of name
    private final int MAX_NAME_LENGTH; // max length of name
    private final int MAX_PAYMENT; // Max Payment allowed
    private final int MIN_PAYMENT; // Max Payment allowed

    private final int MAX_PAYMENT_DOLLAR; //Will be the maximum dollar amount ( calculated from MAX_PAYMENT)
    private final int MAX_PAYMENT_CENTS; //Will be the maximum cents amount ( calculated from MAX_PAYMENT)

    private final String allowableCharactersInName;

    private final List<Frequency> allowableFrequencies;
    private final int MAX_TAGS; // Max number of tags per subscription



    public SubscriptionValidator(int inputMinNameLen, int inputMaxNameLen, int inputMinPayment, int inputMaxPayment, String inputAllowableChars, int inputMaxTags, List<Frequency> inputAllowableFrequencies)
    {

        //Set the various parameters for what is a valid subscription
        this.MIN_NAME_LENGTH = inputMinNameLen;
        this.MAX_NAME_LENGTH = inputMaxNameLen;
        this.MAX_PAYMENT = inputMaxPayment;
        this.MIN_PAYMENT = inputMinPayment;
        this.MAX_PAYMENT_DOLLAR = this.MAX_PAYMENT / 100;
        this.MAX_PAYMENT_CENTS = this.MAX_PAYMENT - this.MAX_PAYMENT_DOLLAR * 100;
        this.allowableCharactersInName = inputAllowableChars;
        this.allowableFrequencies = inputAllowableFrequencies;
        this.MAX_TAGS = inputMaxTags;
    }


    // Validate the inputName.
    // Throws an exception if string invalid
    // Current rules:
     //     Must be a least MIN_NAME_LENGTH long
    //      Must be less than or equal to MAX_NAME_LENGTH characters long
    //      chars are restricted to certain characters (look at allowableCharactersInName)

    public void validateName(final String inputName) throws SubscriptionInvalidNameException {

        //The name has to be a minimum length
        if (inputName.trim().length() < MIN_NAME_LENGTH) {
            throw new SubscriptionInvalidNameException("Name required");
        }

        if (inputName.length() > MAX_NAME_LENGTH)
        {
            throw new SubscriptionInvalidNameException("Name is too long");
        }

        if (!inputName.equals(inputName.trim())) // Don't allow Blank spaces as first or last char
        {

            throw new SubscriptionInvalidNameException("Must not start or end with spaces");
        }

        //Iterate through whole string, and check for invalid characters
        for (int i = 0; i < inputName.length(); i++) {
            if (allowableCharactersInName.indexOf(inputName.charAt(i)) == -1) {
                throw new SubscriptionInvalidNameException(inputName.charAt(i) + " is not an allowable \nchar in name");
            }
        }
    }



    // Validate the whole subscription.
    // Throws exception if object is invalid
    public void validateWholeSubscription(final SubscriptionObj subscriptionToValidate) throws SubscriptionException {
        validateName(subscriptionToValidate.getName());
        validateFrequency(subscriptionToValidate.getPaymentFrequency());
        validatePaymentAmount(subscriptionToValidate.getTotalPaymentInCents());
        validateTagList(subscriptionToValidate.getTagList());

    }

    public void validateTagList(List<SubscriptionTag> tagsToValidate) throws SubscriptionTagException {
        if (tagsToValidate.size() > MAX_TAGS) {
            throw new SubscriptionTagException("Max of " + MAX_TAGS + " tags allowed");
        }

        for (SubscriptionTag currTag : tagsToValidate) {
            //tagHandler.validateTagName(currTag.getName());
        }
    }

    // Validate the Frequency input string
    // Must be one of the allowable frequencies in FrequencyList
    public void validateFrequency(final String inputName) throws SubscriptionInvalidFrequencyException {
        boolean match = false; // A boolean to tell if inputName matches any of our payment Frequencies

        for (Frequency currFrequency : allowableFrequencies) {
            if (currFrequency.checkMatch(inputName)) {
                match = true;
            }
        }
        if (!match) // If there was no match
        {
            throw new SubscriptionInvalidFrequencyException(inputName + " is not a valid frequency");
        }
    }

    // This validates the input Payment amount
    // Throw exceptions if invalid.
    // Currently a valid payment amount > 0, and less than MAX_PAYMENT_CENTS_TOTAL
    public void validatePaymentAmount(final int paymentAmount) throws SubscriptionInvalidPaymentException {
        if (paymentAmount <= MIN_PAYMENT) {
            throw new SubscriptionInvalidPaymentException("Payment amount is too small");
        }
        if (paymentAmount > MAX_PAYMENT) {
            throw new SubscriptionInvalidPaymentException("Payment amount is too large.\n Maximum payment is $" + MAX_PAYMENT_DOLLAR + "." + MAX_PAYMENT_CENTS);

        }
    }

}
