package com.track_it.domainObject;

import java.util.jar.Attributes;

public class SubscriptionObj
{
    //Payment Frequency - Static variables to target
    public static final String WEEKLY = "weekly";
    public static final String MONTHLY = "monthly";
    public static final String YEARLY = "yearly";





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

    public SubscriptionObj copy()
    {
        SubscriptionObj copyOfSubscription = new SubscriptionObj(this.name, this.paymentInCents, this.paymentFrequency);


        return copyOfSubscription;
    }

    public String getName()
    {
        return name;

    }

    public String getPaymentFrequency()
    {
        return paymentFrequency;

    }

    public int getPaymentInCents()
    {
        return paymentInCents;
    }




}
