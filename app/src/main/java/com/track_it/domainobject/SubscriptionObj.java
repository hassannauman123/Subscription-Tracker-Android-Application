package com.track_it.domainobject;
//
// This class is used to represent a subscription object
//


import com.track_it.logic.FrequencyType;

public class SubscriptionObj
{

    private int subscriptionID; // The unique ID of the subscription
    private String name; // The name of the subscription
    private int paymentInCents; // The payment amount in cents
    private String paymentFrequencyName; // - How often a payment happens. (Since this is a string, we will be limiting the allowable payment frequencies to a handful of well defined values)


    public SubscriptionObj(String inputName, int inputPayments, String inputPaymentFrequency )
    {
        this.name = inputName;
        this.paymentInCents = inputPayments;
        this.paymentFrequencyName = inputPaymentFrequency;
    }


    public int getID()
    {
        return subscriptionID;
    }

    public void setID(int inputID)
    {
        subscriptionID = inputID;

    }

    // Create and return a  copy of this object
    public SubscriptionObj copy()
    {
        SubscriptionObj copyOfSubscription = new SubscriptionObj(this.name, this.paymentInCents, this.paymentFrequencyName);
        copyOfSubscription.setID(this.getID());

        return copyOfSubscription;
    }


    //
    //Various get and set methods. They are very simple (at least for now)
    //


    //Get name
    public String getName()
    {
        return name;

    }

    //Set name
    public void setName(String inputName)
    {
         this.name = inputName;

    }

    //Get payment frequency
    public String getPaymentFrequency()
    {
        return paymentFrequencyName;
    }

    //Set payment frequency
    public void  setPaymentFrequency(String inputFrequency)
    {
        this.paymentFrequencyName =  inputFrequency;
    }


    //Return the whole payment amount
    public int getTotalPaymentInCents() { return paymentInCents; }
    public int getPaymentDollars() // Return only the dollars amount of the payment
    {
        return paymentInCents / 100;
    }

    public int getPaymentCents()  //Return only the cents amount (That is the part that comes after the decimal, ie 22.55 (stored as 2255) -> returns 55)
    { return paymentInCents -  ( paymentInCents / 100) *100 ; }

    public void setPayment(int inputPayment)
    {
        this.paymentInCents = inputPayment;
    }


}
