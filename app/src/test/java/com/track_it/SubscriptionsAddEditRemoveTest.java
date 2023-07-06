package com.track_it;


import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.persistence.FakeDataBase;
import com.track_it.presentation.util.SetupParameters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;


public class SubscriptionsAddEditRemoveTest
{

    private SubscriptionHandler subHandle;


    //Setup a fake database each time
    @Before
    public void setTestHandle()
    {
        SetupParameters.InitializeDatabase(new FakeDataBase());
        subHandle =  SetupParameters.GetSubscriptionHandler();
     }


    //Currently we don't have a real database, so there is no point in switching database before running

    //Creates a new subscription object, add it to database, then tries to edit that subscription object (All with valid data)\
    @Test
    public void testAddSub()
    {

        String name = "Valid Name";
        String paymentFrequency = subHandle.getFrequencyList().get(0);
        int paymentAmount = (int) (Math.random() * subHandle.getMaxPaymentCentsTotal()) + 1;


        boolean thrown = false;
        SubscriptionObj newSub = new SubscriptionObj(name,paymentAmount,paymentFrequency);
        try {
            subHandle.addSubscription(newSub);
        }

        catch(Exception e)
        {
            System.out.println("Failed add subscription");
            System.out.println(e.getMessage());
            thrown= true;

        }
        assertFalse("Unable to add new subscription", thrown);

        SubscriptionObj retrieve = subHandle.getSubscriptionByID(newSub.getID());

        //Make sure the sub has the original details
        assertTrue(retrieve.getName() + " does not equal " + name, retrieve.getName().equals(name));

        assertTrue(retrieve.getPaymentFrequency() + " does not equal " + paymentFrequency, retrieve.getPaymentFrequency().equals(paymentFrequency));

        assertEquals(retrieve.getTotalPaymentInCents() + " does not equal " + paymentAmount, retrieve.getTotalPaymentInCents(), paymentAmount);

        System.out.println("Passed the add subscription test!");

    }



    //Creates a new subscription object, add it to database, then tries to edit that subscription object (All with valid data)\
    @Test
    public void testEditValid()
    {

        String Name = "Valid Name";
        String paymentFrequency = subHandle.getFrequencyList().get(0);
        int PaymentAmount = (int) (Math.random() * subHandle.getMaxPaymentCentsTotal()) + 1;

        SubscriptionObj newSub = new SubscriptionObj(Name,PaymentAmount,paymentFrequency);

        try {
            subHandle.addSubscription(newSub);
        }

        catch(Exception e)
        {
            System.out.println("Failed Edit subscription test before running. Failed to add the subscription");
            System.out.println(e.getMessage());

        }


        //Change details of the subscription object
        boolean thrown = false;

        String newName = "New name";
        int newPayment = (int) (Math.random() * subHandle.getMaxPaymentCentsTotal()) + 1;
        while (newPayment == PaymentAmount)
        {
            newPayment = (int) (Math.random() * subHandle.getMaxPaymentCentsTotal()) + 1;
        }
        String newPaymentFrequency = subHandle.getFrequencyList().get(1);

        SubscriptionObj subWithEditDetails = new SubscriptionObj(newName,newPayment,newPaymentFrequency);

        //Try to edit the sub
        try {
            subHandle.editWholeSubscription(newSub.getID(), subWithEditDetails);
        }

        catch(Exception e)
        {
           thrown = true;
           System.out.println(e.getMessage());

        }

        assertFalse("Editing the subscription was unsuccessful", thrown);



        //Make sure the sub details have changed
        SubscriptionObj retrieveSubFomDataBase = subHandle.getSubscriptionByID(newSub.getID());

        assertTrue(retrieveSubFomDataBase.getName() + " does not equal " + newName, retrieveSubFomDataBase.getName().equals(newName));

        assertTrue("was expecting: " + retrieveSubFomDataBase.getPaymentFrequency() + " but instead returned: " + newPaymentFrequency, retrieveSubFomDataBase.getPaymentFrequency().equals(newPaymentFrequency));

        assertEquals(retrieveSubFomDataBase.getTotalPaymentInCents() + " should be equal to " + newPayment, retrieveSubFomDataBase.getTotalPaymentInCents(), newPayment);



        System.out.println("Passed the edit subscription test!");


    }

    @Test
    public void testEditInValid()
    {

        String name = "Valid Name";
        String paymentFrequency = subHandle.getFrequencyList().get(0);
        int paymentAmount = (int) (Math.random() * subHandle.getMaxPaymentCentsTotal()) + 1;

        SubscriptionObj newSub = new SubscriptionObj(name,paymentAmount,paymentFrequency);
        subHandle.addSubscription(newSub);

        boolean thrown = false;

        //Change details of the subscription object with, but have 1 change be invalid
        String newName = "New name";
        int newPayment = 0; // 1 invalid input !

        String newPaymentFrequency = subHandle.getFrequencyList().get(1);

        SubscriptionObj subWithEditDetails = new SubscriptionObj(newName,newPayment,newPaymentFrequency);

        //Try to edit the sub
        try {
            subHandle.editWholeSubscription(newSub.getID(), subWithEditDetails);
        }

        catch(Exception e)
        {
            thrown = true;

        }
        assertTrue("Subscription was edited with invalid input", thrown);




        //Make sure the sub details have NOT changed
        SubscriptionObj retrieveSubFomDataBase = subHandle.getSubscriptionByID(newSub.getID()); //Get the subscription from the database

        assertFalse(retrieveSubFomDataBase.getName() + " should not equal " + newName, retrieveSubFomDataBase.getName().equals(newName));

        assertFalse(retrieveSubFomDataBase.getPaymentFrequency() + " should not equal " + newPaymentFrequency, retrieveSubFomDataBase.getPaymentFrequency().equals(newPaymentFrequency));

        assertTrue("The payment data in the subscription should not have been updated to invalid input", retrieveSubFomDataBase.getTotalPaymentInCents() != newPayment);

        //Make sure the sub has the original details

        assertTrue("The subscription name should not have changed as the input was invalid", retrieveSubFomDataBase.getName().equals(name));

        assertTrue("The subscription pay frequency should not have changed as the input was invalid", retrieveSubFomDataBase.getPaymentFrequency().equals(paymentFrequency));

        assertTrue("The subscription cost should not have changed as the input was invalid", retrieveSubFomDataBase.getTotalPaymentInCents() == paymentAmount);



        System.out.println("Passed the edit subscription test with invalid Edits!");

    }

    @Test
    public void testRemove()
    {

        String name = "Valid Name";
        String paymentFrequency = subHandle.getFrequencyList().get(0);
        int paymentAmount = (int) (Math.random() * subHandle.getMaxPaymentCentsTotal()) + 1;

        SubscriptionObj newSub = new SubscriptionObj(name,paymentAmount,paymentFrequency);

        //ADD SUB
        subHandle.addSubscription(newSub);
        boolean thrown = false;

        //REMOVE SUB
        try {
            subHandle.removeSubscriptionByID(newSub.getID());
        }

        catch(Exception e)
        {
            System.out.println(e.getMessage()  + "one");
            thrown = true;

        }
        assertFalse("Unable to remove subscription", thrown);


        //TRY TO REMOVE SUB again, should be unsuccessful
         thrown = false;
        try {
            subHandle.removeSubscriptionByID(newSub.getID());
        }

        catch(Exception e)
        {
             thrown = true;
        }
        assertTrue("Sub removal method worked for a subscription object that should no longer be in the database", thrown);


        System.out.println("Passed the remove subscription test !");

    }

}
