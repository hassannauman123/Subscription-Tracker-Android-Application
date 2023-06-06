package com.track_it.domainObject;

import java.util.jar.Attributes;

public class SubscriptionObj
{

    private int subscriptionID;
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
        return subscriptionID;
    }

    public void setID(int inputID)
    {
        subscriptionID = inputID;

    }




    public SubscriptionObj copy()
    {
        SubscriptionObj copyOfSubscription = new SubscriptionObj(this.name, this.paymentInCents, this.paymentFrequency);
        copyOfSubscription.setID(this.getID());

        return copyOfSubscription;
    }

    public String getName()
    {
        return name;

    }

    public void  setName(String inputName)
    {
         this.name = inputName;

    }

    public String getPaymentFrequency()
    {
        return paymentFrequency;
    }
    public void  setPaymentFrequency(String inputFrequency)
    {
        this.paymentFrequency =  inputFrequency;
    }

    public int getTotalPaymentInCents() { return paymentInCents; }
    public int getPaymentDollars()
    {
        return paymentInCents / 100;
    }

    public int getPaymentCents() { return paymentInCents -  ( paymentInCents / 100) *100 ; }

    public void setPayment(int inputPayment)
    {
        this.paymentInCents = inputPayment;
    }


}
