package com.track_it.presentation;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.track_it.logic.exception.SubscriptionException;
import com.track_it.logic.exception.SubscriptionInvalidPaymentException;
import com.track_it.logic.SubscriptionHandler;
//
//  This is a general helper class, used to get user input using common functions.
//



public class SubscriptionInput extends AppCompatActivity
{

    // Get the input payment amount that the user entered.
    public int getPaymentAmountInput(EditText inputLocation ) throws SubscriptionInvalidPaymentException
    {

        SubscriptionHandler handler = new  SubscriptionHandler();
        EditText textInput = inputLocation;
        String[] paymentAmountString = textInput.getText().toString().split("\\.");
        int paymentInCents = Integer.MIN_VALUE;


        // Anyways, get Payment amount in cents
        if (paymentAmountString.length > 0) {
            if (isParsable(paymentAmountString[0])) {
                paymentInCents = Integer.parseInt(paymentAmountString[0]) * 100;
            }

            if (paymentAmountString.length > 1) {

                if (isParsable(paymentAmountString[1]))
                {

                    if (paymentInCents == Integer.MIN_VALUE) {
                        paymentInCents = 0;

                    }
                    if(paymentAmountString[1].length() == 1)  // The payment is in the format like: 10.2, so we need to to multiply the 2 by 10 to get cents payment amount
                    {
                        paymentInCents = paymentInCents + Integer.parseInt(paymentAmountString[1]) * 10;
                    }
                    else if ( paymentAmountString[1].length() > 2) // If there are more than 2 digits after decimal, then it is invalid.  ex  10.444 <- more than 2 digits after decimal
                        {
                            paymentInCents = Integer.MIN_VALUE;
                        }


                    else {
                        paymentInCents = paymentInCents + Integer.parseInt(paymentAmountString[1]);
                    }
                }
            }
        }
        else {
            throw new SubscriptionInvalidPaymentException("Invalid payment amount");
        }

        if (paymentInCents == Integer.MIN_VALUE) {

            throw new SubscriptionInvalidPaymentException("Invalid payment amount");

        } else {
            try {
                handler.validatePaymentAmount(paymentInCents);

            } catch (SubscriptionException e) {
                throw new SubscriptionInvalidPaymentException(e.getMessage());

            }

        }

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
    public static int NumDigits(int dollarAmount)
    {
        int payment = dollarAmount;
        int digitsBeforeDecimalCount = 0;

        while (payment != 0) {
            payment = payment / 10;
            digitsBeforeDecimalCount++;
        }
        return digitsBeforeDecimalCount;
    }

}



//Limits the user input for a decimal input to a format such that there will be a max of digitBeforeDecimal before decimal and a max of decimalDigits
// !* reminder this is not complete, still allows decimals to go in wrong places sometimes.
class DecimalDigitsInputFilter implements InputFilter {

    private final int decimalDigits; // Maximum number of integer after decimal
    private final int digitBeforeDecimal; // Maximum number of integers before deciamls

    public DecimalDigitsInputFilter(int decimalDigits, int digitBeforeDecimal) {
        this.decimalDigits = decimalDigits;
        this.digitBeforeDecimal = digitBeforeDecimal;
    }


    // Makes it such that the string the user is allowed to entered will be in the format such that the number of digits before the decimal will be at most digitBeforeDecimal
    // and the number of digits after the decimal will be decimalDigits
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        //Reminder
        // source is the char being added.
        // destination is the previous string. Source will be added to destination at position dstart if we return null, else it will be reject if we return ""


        CharSequence returnValue = null; // Null means we accept source, "" means we reject it

        int dotPos = -1; // Where does the decimal occur in destination?
        int len = dest.length(); // Get length of previous string



            // Iterate over the string, looking for decimal character
            for (int i = 0; i < len; i++) {
                char charIterator = dest.charAt(i); // The current char being read

                if (charIterator == '.') {
                    dotPos = i; // Decimal place found
                    break;
                } else if ((i >= digitBeforeDecimal - 1) && !source.equals("."))  // The maximum amount of integers before the decimal

                {
                    if ((i + 1) < len && dest.charAt(i + 1) == '.') // The next char after this is a decimal, so we are good
                    {
                        continue; // Continue back to loop ( dotPos will end being = to i+1);
                    } else {
                        returnValue = ""; // Else we hit the maximum number of integers before decimal, and the next char is not a decimal so don't add anything
                        break; // Break out of this for loop, and dotPos will <
                    }
                }
            }


            if (dotPos >= 0)  // Decimal is in the string, and we are trying to insert a digit
            {

                // If source is being added to front of decimal and there is room accept source
                if (dend <= dotPos && (dotPos < digitBeforeDecimal)) {
                    returnValue = null;
                }
                // Else - If there are already 2 digits past the decimal and source digit is going past decimal reject source
                else if (len - dotPos > decimalDigits) {
                    returnValue = "";
                }
            }


        return returnValue; // Will be null if source accepted, and "" if source rejected!
    }

}

