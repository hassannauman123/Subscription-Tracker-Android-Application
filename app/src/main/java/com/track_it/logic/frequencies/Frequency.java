package com.track_it.logic.frequencies;


public interface Frequency
{
    boolean checkMatch(String inputCheck); // Does ths input string match the frequency?
    String getFrequencyName(); // get name of frequency

    int calculateMonthlyPayment(int paymentAmount); // calculate monthly payment

    int daysBetweenPayment(); // Calculate days between payment (rounded down to nearest int)


}










