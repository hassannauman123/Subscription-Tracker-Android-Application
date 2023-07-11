package com.track_it;


import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.comparators.CompareSubscriptionFrequency;
import com.track_it.logic.comparators.CompareSubscriptionName;
import com.track_it.logic.comparators.CompareSubscriptionPayment;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.frequencies.Frequency;
import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.fakes.FakeSubscriptionPersistenceDatabase;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;
import com.track_it.presentation.util.SetupParameters;
import com.track_it.util.FillDataBase;
import com.track_it.util.TestUtils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SubscriptionSortTest
{


    private SubscriptionHandler subHandle;
    private  List<SubscriptionObj> listOfSubs;

    @Before
    public void setTestHandle()
    {

        TestUtils.changeDatabase();

        subHandle = SetupParameters.getSubscriptionHandler();
        FillDataBase.fillDataBaseRandomSubscriptions(subHandle, 3); //Create 100 fake subs, and add them to Database
        listOfSubs = subHandle.getAllSubscriptions(); // Get subscriptions from database

    }



    @Test
    public void testSortByName()
    {

        confirmSortName();


        //Let random data
        subHandle = SetupParameters.getSubscriptionHandler();
        FillDataBase.fillFakeData(subHandle); //Fill database with a few non random inputs
        TestUtils.changeDatabase();


        confirmSortName();


        System.out.println("PASSED the sort by name test");
    }

    public void confirmSortName()
    {
        Collections.sort(listOfSubs,new CompareSubscriptionName()); //Sort by name


        for ( int i = 0; i < listOfSubs.size() -1 ; i ++)
        {

            String firstName = listOfSubs.get(i).getName();
            String secondName = listOfSubs.get(i+1).getName();
            System.out.println(firstName);
            assertTrue( "Failed sortName test\n. " + firstName + " was before " +  secondName + ". After sorting by name" ,firstName.toLowerCase().compareTo(secondName.toLowerCase()) <= 0);
        }
    }


    @Test
     public void testSortByPayment()
    {

        confirmSortPayment();


        //Let random data
        subHandle = SetupParameters.getSubscriptionHandler();
        FillDataBase.fillFakeData(subHandle); //Fill database with a few non random inputs
        TestUtils.changeDatabase();

        confirmSortPayment();


        System.out.println("PASSED the sort by payment test ");
    }


    public void confirmSortPayment()
    {


        Collections.sort(listOfSubs,new CompareSubscriptionPayment() ); //Sort by payment amount

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

        confirmFrequency();

        //Less random data
        subHandle = SetupParameters.getSubscriptionHandler();
        FillDataBase.fillFakeData(subHandle); //Fill database with a few non random inputs
        TestUtils.changeDatabase();

        confirmFrequency();



        System.out.println("PASSED the sort by payment frequency test");

    }


    private void confirmFrequency()
    {


        Collections.sort(listOfSubs,new CompareSubscriptionFrequency()); // Sort by frequency amount

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
