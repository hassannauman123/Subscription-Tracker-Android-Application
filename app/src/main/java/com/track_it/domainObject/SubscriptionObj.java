package com.track_it.domainObject;

public class SubscriptionObj
{
    //Payment Frequency - Static variables to target

    public static final String WEEKLY = "weekly";
    public static final String MONTHLY = "monthly";
    public static final String YEARLY = "yearly";

    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_PAYMENT = 5;



    private int uniqueId;
    private String name;
    private int paymentInCents;
    private String paymentFrequency; // If we limit ourselves to this only being a string then we will forever limit the input of the user (At least not without a lot of rework. I am not sure if that is a good idea or not

    public SubscriptionObj(String inputName, int inputPayments, String inputPaymentFrequency )
    {
        this.name = inputName;
        this.paymentInCents = inputPayments;
        this.paymentFrequency = inputPaymentFrequency;
    }

    public int getID()
    {
        return uniqueId;
    }

    public void setID(int inputID)
    {
        uniqueId = inputID;

    }



}
