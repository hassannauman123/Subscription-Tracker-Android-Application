package com.track_it;


import com.track_it.domainObject.SubscriptionObj;
import com.track_it.persistence.DataBase;
import com.track_it.logic.SubscriptionHandler;

import org.junit.Test;
import com.track_it.domainObject.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class SubscriptionsAddEditRemoveTest
{

    //Currently we don't have a real database, so there is no point in switching database before running

    //Creates a new subscription object, add it to database, then tries to edit that subscription object (All with valid data)\
    @Test
    public void testAddSub()
    {
        SubscriptionHandler subHandle = new SubscriptionHandler();

        String name = "Valid Name";
        String paymentFrequency = SubscriptionHandler.getFrequencyList()[0];
        int paymentAmount = (int) (Math.random() * SubscriptionHandler.getMaxPaymentCentsTotal()) + 1;


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
        assertFalse(thrown);

        SubscriptionObj retrieve = subHandle.getSubscriptionByID(newSub.getID());

        //Make sure the sub has the original details

        assertTrue(retrieve.getName().equals(name));

        assertTrue(retrieve.getPaymentFrequency().equals(paymentFrequency));

        assertTrue(retrieve.getTotalPaymentInCents() == paymentAmount);



        System.out.println("Passed the add subscription test!");


    }



    //Creates a new subscription object, add it to database, then tries to edit that subscription object (All with valid data)\
    @Test
    public void testEditValid()
    {
        SubscriptionHandler subHandle = new SubscriptionHandler();

        String Name = "Valid Name";
        String paymentFrequency = SubscriptionHandler.getFrequencyList()[0];
        int PaymentAmount = (int) (Math.random() * SubscriptionHandler.getMaxPaymentCentsTotal()) + 1;

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
        int newPayment = (int) (Math.random() * SubscriptionHandler.getMaxPaymentCentsTotal()) + 1;
        while (newPayment == PaymentAmount)
        {
            newPayment = (int) (Math.random() * SubscriptionHandler.getMaxPaymentCentsTotal()) + 1;
        }
        String newPaymentFrequency = SubscriptionHandler.getFrequencyList()[1];

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

        assertFalse(thrown);



        //Make sure the sub details have changed
        SubscriptionObj retrieveSubFomDataBase = subHandle.getSubscriptionByID(newSub.getID());

        assertTrue(retrieveSubFomDataBase.getName().equals(newName));

        assertTrue(retrieveSubFomDataBase.getPaymentFrequency().equals(newPaymentFrequency));

        assertTrue(retrieveSubFomDataBase.getTotalPaymentInCents() == newPayment);



        System.out.println("Passed the edit subscription test!");


    }

    @Test
    public void testEditInValid()
    {
        SubscriptionHandler subHandle = new SubscriptionHandler();

        String name = "Valid Name";
        String paymentFrequency = SubscriptionHandler.getFrequencyList()[0];
        int paymentAmount = (int) (Math.random() * SubscriptionHandler.getMaxPaymentCentsTotal()) + 1;

        SubscriptionObj newSub = new SubscriptionObj(name,paymentAmount,paymentFrequency);

        boolean thrown = false;

        try {
            subHandle.addSubscription(newSub);
        }

        catch(Exception e)
        {
            System.out.println("Failed Edit subscription test before running. Failed to add the subscription");
            System.out.println(e.getMessage());
            thrown = true;

        }
        assertFalse(thrown);


        //Change details of the subscription object with, but have 1 change be invalid
         thrown = false;

        String newName = "New name";
        int newPayment = 0; // 1 invalid input !

        String newPaymentFrequency = SubscriptionHandler.getFrequencyList()[1];

        SubscriptionObj subWithEditDetails = new SubscriptionObj(newName,newPayment,newPaymentFrequency);

        //Try to edit the sub
        try {
            subHandle.editWholeSubscription(newSub.getID(), subWithEditDetails);
        }

        catch(Exception e)
        {
            thrown = true;

        }

        assertTrue(thrown);




        //Make sure the sub details have NOT changed
        SubscriptionObj retrieveSubFomDataBase = subHandle.getSubscriptionByID(newSub.getID()); //Get the subscription from the database

        assertTrue(!retrieveSubFomDataBase.getName().equals(newName));

        assertTrue(!retrieveSubFomDataBase.getPaymentFrequency().equals(newPaymentFrequency));

        assertTrue(retrieveSubFomDataBase.getTotalPaymentInCents() != newPayment);

        //Make sure the sub has the original details

        assertTrue(retrieveSubFomDataBase.getName().equals(name));

        assertTrue(retrieveSubFomDataBase.getPaymentFrequency().equals(paymentFrequency));

        assertTrue(retrieveSubFomDataBase.getTotalPaymentInCents() == paymentAmount);



        System.out.println("Passed the edit subscription test with invalid Edits!");

    }

    @Test
    public void testRemove()
    {
        SubscriptionHandler subHandle = new SubscriptionHandler();

        String name = "Valid Name";
        String paymentFrequency = SubscriptionHandler.getFrequencyList()[0];
        int paymentAmount = (int) (Math.random() * SubscriptionHandler.getMaxPaymentCentsTotal()) + 1;

        SubscriptionObj newSub = new SubscriptionObj(name,paymentAmount,paymentFrequency);

        //ADD SUB
        boolean thrown = false;
        try {
            subHandle.addSubscription(newSub);
        }

        catch(Exception e)
        {
            System.out.println("Failed remove subscription test before running. Failed to add the subscription");
            System.out.println(e.getMessage());
            thrown = true;

        }
        assertFalse(thrown);


        //REMOVE SUB
         thrown = false;
        try {
            subHandle.removeSubscriptionByID(newSub.getID());
        }

        catch(Exception e)
        {
            System.out.println(e.getMessage()  + "one");
            thrown = true;

        }
        assertFalse(thrown);


        //TRY TO REMOVE SUB again
         thrown = false;
        try {
            subHandle.removeSubscriptionByID(newSub.getID());
        }

        catch(Exception e)
        {
             thrown = true;

        }
        assertTrue(thrown);




        System.out.println("Passed the remove subscription test !");

    }

}
