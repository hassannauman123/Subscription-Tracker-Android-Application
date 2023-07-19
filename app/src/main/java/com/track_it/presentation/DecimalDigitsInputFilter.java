package com.track_it.presentation;

import android.text.InputFilter;
import android.text.Spanned;


//Limits the user input for a decimal input to a format such that there will be a max of digitBeforeDecimal before decimal and a max of decimalDigits
// !* reminder this is not complete, still allows decimals to go in wrong places sometimes.
public class DecimalDigitsInputFilter implements InputFilter {

    private final int decimalDigitsAfter; // Maximum number of integer after decimal point
    private final int digitBeforeDecimal; // Maximum number of integers before decimal point

    public DecimalDigitsInputFilter(int decimalDigits, int digitBeforeDecimal) {
        this.decimalDigitsAfter = decimalDigits;
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


        //Prevent a decimal from being added in front of more than 2 integers!
        if (source.equals("."))
        {
            if ( dest.toString().length() - dstart > decimalDigitsAfter)
            {
                returnValue = "";
            }

        }

        else //Else the new character was not decimal point
        {
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
                else if (len - dotPos > decimalDigitsAfter) {
                    returnValue = "";
                }
            }
        }


        return returnValue; // Will be null if source accepted, and "" if source rejected!
    }

}