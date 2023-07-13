package com.track_it.presentation.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import com.track_it.logic.exceptions.SubscriptionInvalidPaymentException;
import com.track_it.logic.SubscriptionHandler;
//
//  This is a general helper class, used to get user input using common functions.
//


public class SubscriptionInput {
    private final SubscriptionHandler subHandle;

    public SubscriptionInput(SubscriptionHandler inputSubHandle)
    {
        subHandle = inputSubHandle;
    }


    // Get the input payment amount that the user entered.
    public int getPaymentAmountInput(EditText inputLocation) throws SubscriptionInvalidPaymentException
    {

        String[] paymentAmountString = inputLocation.getText().toString().split("\\.");
        int paymentInCents = Integer.MIN_VALUE;


        // Get Payment amount in cents
        if (paymentAmountString.length > 0) {
            if (isParsable(paymentAmountString[0])) {
                paymentInCents = Integer.parseInt(paymentAmountString[0]) * 100;
            }

            if (paymentAmountString.length > 1) {

                if (isParsable(paymentAmountString[1])) {

                    if (paymentInCents == Integer.MIN_VALUE) {
                        paymentInCents = 0;

                    }
                    if (paymentAmountString[1].length() == 1)  // The payment is in the format like: 10.2, so we need to to multiply the 2 by 10 to get cents payment amount
                    {
                        paymentInCents = paymentInCents + Integer.parseInt(paymentAmountString[1]) * 10;
                    } else if (paymentAmountString[1].length() > 2) // If there are more than 2 digits after decimal, then it is invalid.  ex  10.444 <- more than 2 digits after decimal
                    {
                        paymentInCents = Integer.MIN_VALUE;
                    } else {
                        paymentInCents = paymentInCents + Integer.parseInt(paymentAmountString[1]);
                    }
                }
            }
        }


        subHandle.validatePaymentAmount(paymentInCents); // Try to validate payment amount. Will throw exception if invalid.


        return paymentInCents;

    }



    // check if the input string is parsable by Integer.parseInt function
    private boolean isParsable(String inputString) {
        try {
            Integer.parseInt(inputString);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }


    //This function will return how many digits are in dollarAmount
    public static int NumDigits(int dollarAmount) {
        int payment = dollarAmount;
        int digitsBeforeDecimalCount = 0;

        while (payment != 0) {
            payment = payment / 10;
            digitsBeforeDecimalCount++;
        }
        return digitsBeforeDecimalCount;
    }

}


