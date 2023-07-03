

package com.track_it.presentation;

import com.track_it.logic.SubscriptionHandler;

public class SetupParameters
{

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 30;
    private static final int MAX_PAYMENT_DOLLAR = 9999;
    private static final int MAX_PAYMENT_CENTS = 99;

    private static final String allowableCharactersInName = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 _+*^&%$#@!+=|}]'?<>'"; // Our current list of allowable characters in the name



    //Wrapper for the logic layer
    public static SubscriptionHandler InitializeLogicLayer()
    {
        return new SubscriptionHandler(MIN_NAME_LENGTH,MAX_NAME_LENGTH,MAX_PAYMENT_DOLLAR,MAX_PAYMENT_CENTS,allowableCharactersInName );
    }


    // Returns how many digits are allowable before decimal place for payment amount=
    public  static int getMaxPaymentDigitsBeforeDecimal() {
        int payment = MAX_PAYMENT_DOLLAR;
        int digitsBeforeDecimalCount = 0;

        while (payment != 0) {
            payment = payment / 10;
            digitsBeforeDecimalCount++;
        }

        return digitsBeforeDecimalCount;
    }


}
