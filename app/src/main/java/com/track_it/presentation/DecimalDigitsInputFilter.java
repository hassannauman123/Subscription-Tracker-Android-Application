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

        String newString = dest.toString().substring(0, dstart) + source.toString() + dest.toString().substring(dend, dest.toString().length()); //What the new string will be if we accept


        String splitAlongDecimal[] = newString.split("\\."); // Split string up along decimal points


        if (splitAlongDecimal.length > 0 && splitAlongDecimal[0].length() > this.digitBeforeDecimal) //Only accept input if there are no more than digitBeforeDecimal number of decimals before decimal point
        {
            returnValue = "";
        }
        else if (splitAlongDecimal.length > 1 && splitAlongDecimal[1].length() > this.decimalDigitsAfter) // Else - only accept if there is a correct number of digits after decimal
        {
            returnValue = "";
        }

       else if (splitAlongDecimal.length > 2) //Only accept if there is 1 decimal
        {
            returnValue = "";
        }


        return returnValue; // Will be null if source accepted, and "" if source rejected!
    }

}