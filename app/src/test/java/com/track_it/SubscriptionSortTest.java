package com.track_it;


import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionCompare.CompareSubscriptionFrequency;
import com.track_it.logic.SubscriptionCompare.CompareSubscriptionName;
import com.track_it.logic.SubscriptionCompare.CompareSubscriptionPayment;
import com.track_it.logic.SubscriptionCompare.SubscriptionCompare;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.SubscriptionSorter;
import com.track_it.logic.frequencies.Frequency;
import com.track_it.persistence.FakeDataBase;
import com.track_it.presentation.util.SetupParameters;
import com.track_it.util.FillDataBase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

import java.util.List;

public class SubscriptionSortTest
{


    private SubscriptionHandler subHandle;
    private  List<SubscriptionObj> listOfSubs;

    @Before
    public void setTestHandle()
    {

        SetupParameters.initializeDatabase(new FakeDataBase());
        subHandle =  SetupParameters.getSubscriptionHandler();

        FillDataBase.fillDataBaseRandomSubscriptions(subHandle, 100); //Create 100 fake subs, and add them to Database
        listOfSubs = subHandle.getAllSubscriptions(); // Get subscriptions from database


    }

    @Test
    public void testSortByName()
    {
        SubscriptionCompare subCompare = new CompareSubscriptionName();

        SubscriptionSorter subSorter = new SubscriptionSorter(subCompare);
        subSorter.sortSubscriptions(listOfSubs); // Sort by name


        for ( int i = 0; i < listOfSubs.size() -1 ; i ++)
        {
            String firstName = listOfSubs.get(i).getName();
            String secondName = listOfSubs.get(i+1).getName();
            assertTrue( "Failed sortName test\n. " + firstName + " was before " +  secondName + ". After sorting by name" ,firstName.toLowerCase().compareTo(secondName.toLowerCase()) <= 0);
        }

    }


    @Test
    public void testSortByPayment()
    {
        SubscriptionCompare subCompare = new CompareSubscriptionPayment();


        SubscriptionSorter subSorter = new SubscriptionSorter(subCompare);
        subSorter.sortSubscriptions(listOfSubs); // Sort by Payment amount

        for ( int i = 0; i < listOfSubs.size() -1 ; i ++)
        {
            int firstPay= listOfSubs.get(i).getTotalPaymentInCents();
            int secondPay = listOfSubs.get(i+1).getTotalPaymentInCents();
            assertTrue( "Failed sortPayment test\n. " + firstPay + " was before " +  secondPay + ". After sorting by payment" ,firstPay <= secondPay);
        }

    }


    @Test
    public void testSortFrequency()
    {
        SubscriptionCompare subCompare = new CompareSubscriptionFrequency();
        SubscriptionSorter subSorter = new SubscriptionSorter(subCompare);
        subSorter.sortSubscriptions(listOfSubs); // Sort by frequency amount

        for ( int i = 0; i < listOfSubs.size() -1 ; i ++)
        {
            String firstFrequencyName = listOfSubs.get(i).getPaymentFrequency();
            String secondFrequencyName = listOfSubs.get(i+1).getPaymentFrequency();

            Frequency firstFrequency =  subHandle.getFrequencyObject(listOfSubs.get(i));
            Frequency secondFrequency = subHandle.getFrequencyObject(listOfSubs.get(i+1));
            assertTrue( "Failed frequency test\n. " + firstFrequencyName + " was before " +  secondFrequencyName + ". After sorting by frequency" ,firstFrequency.daysBetweenPayment() <= secondFrequency.daysBetweenPayment());
        }

    }


}
