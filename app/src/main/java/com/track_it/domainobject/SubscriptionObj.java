package com.track_it.domainobject;


//
// This class is used to represent a subscription object
//

import java.util.ArrayList;
import java.util.List;

public class SubscriptionObj {

    private int subscriptionID; // The unique ID of this subscription
    private String name; // The name of the subscription
    private int paymentInCents; // The payment amount in cents
    private String paymentFrequencyName; // - How often a payment happens.
    private List<SubscriptionTag> subscriptionTagList;

    public SubscriptionObj(String inputName, int inputPayments, String inputPaymentFrequency)
    {
        this.subscriptionTagList = new ArrayList<>();
        this.name = inputName;
        this.paymentInCents = inputPayments;
        this.paymentFrequencyName = inputPaymentFrequency;
    }


    public void setTagList(List<SubscriptionTag> inputList) {
        this.subscriptionTagList = inputList;
    }


    public List<SubscriptionTag> getTagList() {
        return subscriptionTagList;
    }

    public int getID() {
        return subscriptionID;
    }

    public void setID(int inputID) {
        this.subscriptionID = inputID;

    }

    // Create and return a  copy of this object
    public SubscriptionObj copy() {
        SubscriptionObj copyOfSubscription = new SubscriptionObj(this.name, this.paymentInCents, this.paymentFrequencyName);
        copyOfSubscription.setID(this.getID());

        return copyOfSubscription;
    }


    //
    //Various get and set methods. They are very simple (at least for now)
    //


    //Get name
    public String getName() {
        return this.name;

    }

    //Set name
    public void setName(String inputName) {
        this.name = inputName;

    }

    //Get payment frequency
    public String getPaymentFrequency() {
        return paymentFrequencyName;
    }

    //Set payment frequency
    public void setPaymentFrequency(String inputFrequency) {
        this.paymentFrequencyName = inputFrequency;
    }


    //Return the whole payment amount
    public int getTotalPaymentInCents() {
        return paymentInCents;
    }

    public int getPaymentDollars() // Return only the dollars amount of the payment
    {
        return this.paymentInCents / 100;
    }

    public int getPaymentCents()  //Return only the cents amount (That is the part that comes after the decimal, ie 22.55 (stored as 2255) -> returns 55)
    {
        return this.paymentInCents - (this.paymentInCents / 100) * 100;
    }

    public void setPayment(int inputPayment) {
        this.paymentInCents = inputPayment;
    }


}
