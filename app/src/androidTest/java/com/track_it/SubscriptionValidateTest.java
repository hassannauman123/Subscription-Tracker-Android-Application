package com.track_it;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.track_it.logic.SubscriptionHandler;

import org.junit.Test;


public class SubscriptionValidateTest {

    @Test
    // We are going to test the validate name test
    public void testNameValidateWithInValidData() {
        SubscriptionHandler subHandler = new SubscriptionHandler();

        boolean thrown = false;
        String inputName = "";
        try {
            subHandler.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);


        // Try lots of blank space
        for (int i = 0; i < 100; i++) {
            inputName = inputName + " ";
            thrown = false;
            try {
                subHandler.validateName(inputName);
            } catch (Exception e) {
                thrown = true;
            }
            assertTrue(thrown);
        }


        // Try to short letters
        thrown = false;
        inputName = "ab";

        try {
            subHandler.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);


        // Try non alpha letters first
        thrown = false;
        inputName = "1ab";

        try {
            subHandler.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);


        // Try white spaces first
        thrown = false;
        inputName = " onettwo";

        try {
            subHandler.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);


        // Try white spaces trailing
        thrown = false;
        inputName = "onettwo ";

        try {
            subHandler.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);


        // Invalid characters
        thrown = false;
        inputName = "One~Twothree` ";

        try {
            subHandler.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);

        System.out.println("Finished testing validateName with invalid Data!");
    }

    @Test
    // We are going to test the validateName function with valid names
    public void testNameValidateWithValidData() {
        SubscriptionHandler subHandler = new SubscriptionHandler();

        // Valid 3 letter name
        boolean thrown = false;
        String inputName = "One";

        try {
            subHandler.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(!thrown);


        // Valid name with alpha characters
        thrown = false;
        inputName = "On3ee3";

        try {
            subHandler.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(!thrown);


        // Valid  name with spaces one space in middle
        thrown = false;
        inputName = "One Two";

        try {
            subHandler.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(!thrown);


        // Valid  name with two spaces in middle
        thrown = false;
        inputName = "One  Two";

        try {
            subHandler.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(!thrown);


        // Valid 100 Char name
        thrown = false;
        inputName = "a23";
        String allowChars = subHandler.getAllowableChars();


        for (int i = 0; i < subHandler.getMaxNameLength() - 3; i++) {
            char newChar = allowChars.charAt((int) (Math.random() * allowChars.length()));

            while (newChar == ' ') // The last letter of  name cannot be a space!!!
            {
                newChar = allowChars.charAt((int) (Math.random() * allowChars.length()));
            }

            inputName = inputName + newChar;
            try {
                subHandler.validateName(inputName);
            } catch (Exception e) {
                System.out.println("Name:  " + inputName + " was invalid.  Thrown on index " + i);
                System.out.println(e.getMessage());
                thrown = true;
            }
            assertTrue(!thrown);
        }

        System.out.println("Finished testing validateName with valid Data!");
    }

    // Test Validate payment frequency with invalid Data
    @Test
    public void testValidatePaymentFrequencyWithInValidData() {


        boolean thrown = false;
        String inputFrequency = "";
        SubscriptionHandler subHandler = new SubscriptionHandler();


        // Invalid data with empty string
        thrown = false;
        inputFrequency = "";

        try {
            subHandler.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);

        // Invalid data with blank space
        thrown = false;
        inputFrequency = "  ";

        try {
            subHandler.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);

        // Invalid data with strange looking input
        thrown = false;
        inputFrequency = " one two";

        try {
            subHandler.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);


        // Invalid data with correct frequency + char
        thrown = false;
        inputFrequency = subHandler.getFrequencyList()[0] + "1";

        try {
            subHandler.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);




        // Invalid data with correct frequency + blank character
        thrown = false;
        inputFrequency = subHandler.getFrequencyList()[1] + " ";

        try {
            subHandler.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);


        // Invalid data with correct frequency + blank character
        thrown = false;
        inputFrequency = " " + subHandler.getFrequencyList()[1];

        try {
            subHandler.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);


        System.out.println("Finished testing validatePaymentFrequency with invalid Data!");


    }


    // Test Validate payment frequency with invalid Data
    @Test
    public void testValidatePaymentFrequencyWithValidData() {

        boolean thrown = false;
        String inputFrequency = "";
        SubscriptionHandler subHandler = new SubscriptionHandler();


        // Invalid data with correct frequency + char
        thrown = false;
        String allowableFrequency[] = subHandler.getFrequencyList();

        for (int i = 0; i < allowableFrequency.length; i++)
        {
            thrown = false;
            inputFrequency = allowableFrequency[i];
            try {
                subHandler.validateFrequency(inputFrequency);

            }
            catch ( Exception e)
            {
                System.out.println("validate frequency Test failed with input " + inputFrequency);
                System.out.println(e.getMessage());
                thrown = true;
            }

            assertTrue(!thrown);
        }


        System.out.println("Finished testing validatePaymentFrequency with valid Data!");


    }


    // Test Validate paymentAmount with invalid Data
    @Test
    public void testValidatePaymentAmountWithInvalidData() {

        int paymentAmount = 0;
        boolean thrown = false;
        SubscriptionHandler subHandler = new SubscriptionHandler();


        // Payment too small
        thrown = false;
        paymentAmount = 0;
        try {
            subHandler.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);


        // Payment too small
        thrown = false;
        paymentAmount = -1;
        try {
            subHandler.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);


        // Payment too small
        thrown = false;
        paymentAmount = Integer.MIN_VALUE;
        try {
            subHandler.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);

        // Payment too large
        thrown = false;
        paymentAmount =  Integer.MAX_VALUE;
        try {
            subHandler.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);

        // Payment too large
        thrown = false;
        paymentAmount =  subHandler.getMaxPaymentCentsTotal() + 1;
        try {
            subHandler.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);
        System.out.println("Finished testing validatePaymentAmount with invalid Data!");

    }


    // Test Validate payment frequency with valid Data
    @Test
    public void testValidatePaymentAmountWithvalidData() {


        int paymentAmount = 0;
        boolean thrown = false;
        SubscriptionHandler subHandler = new SubscriptionHandler();


        // Payment correct
        thrown = false;
        paymentAmount = 1;
        try {
            subHandler.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(!thrown);


        int maxPayment = subHandler.getMaxPaymentCentsTotal();
        //Cycle through all correct payments
       for ( int i = 1 ; i <=maxPayment; i++ )
       {

           thrown = false;
           paymentAmount= i;
           try {
               subHandler.validatePaymentAmount(paymentAmount);
           } catch (Exception e) {

               System.out.println("Validate payment amount failed with input "+ i);
               thrown = true;
           }

           assertFalse(thrown);

       }


        System.out.println("Finished testing validatePaymentAmount with valid Data!");

    }






}



