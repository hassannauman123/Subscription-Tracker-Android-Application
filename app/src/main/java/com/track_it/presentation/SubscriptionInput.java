package com.track_it.presentation;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import com.track_it.logic.exceptions.SubscriptionInvalidPaymentException;
import com.track_it.logic.SubscriptionHandler;

//
//  This is a class used to get payment input from a EditText object
//
public class SubscriptionInput {


    // Get the input payment amount that the user entered.
    public int getPaymentAmountInput(EditText inputLocation) {

        String[] paymentAmountString = inputLocation.getText().toString().split("\\."); // Break up string along decimal
        int paymentInCents = 0;


        // Get Payment amount in cents
        if (paymentAmountString.length > 0) {

            if (isParsable(paymentAmountString[0]))  // First covert the dollar amount to cents ( the part before the decimal)
            {
                paymentInCents = Integer.parseInt(paymentAmountString[0]) * 100;
            }

            if (paymentAmountString.length > 1)  // If there was a decimal
            {
                if (isParsable(paymentAmountString[1]))
                {
                    if (paymentAmountString[1].length() == 1)  // The payment is in the format like: 10.2, so we need to to multiply the 2 by 10 to get cents payment amount
                    {
                        paymentInCents = paymentInCents + Integer.parseInt(paymentAmountString[1]) * 10;
                    } else {
                        paymentInCents = paymentInCents + Integer.parseInt(paymentAmountString[1]); // Else just add as cents
                    }
                }
            }
        }

        return paymentInCents;

    }


    // check if the input string is parsable by Integer.parseInt function
    private boolean isParsable(String inputString) {
        boolean isStringParsableToInt;

        try {
            Integer.parseInt(inputString);
            isStringParsableToInt = true;
        } catch (final NumberFormatException e) {
            isStringParsableToInt = false;
        }

        return isStringParsableToInt;
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


