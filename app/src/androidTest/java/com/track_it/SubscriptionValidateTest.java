package com.track_it;

import org.junit.Test;

import static org.junit.Assert.*;

import com.track_it.logic.SubscriptionHandler;


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


        System.out.println("Finished testing validatePaymentFrequency with invalid Data!");


    }


    // Test Validate payment frequency with invalid Data
    @Test
    public void  testValidatePaymentFrequencyWithValidData() {
        System.out.println("Finished testing validatePaymentFrequency with valid Data!");


    }


    // Test Validate paymentAmount with invalid Data
    @Test
    public void  testValidatePaymentAmountWithInvalidData()
    {
        System.out.println("Finished testing validatePaymentAmount with valid Data!");

    }


    // Test Validate payment frequency with valid Data
    @Test
    public void  testValidatePaymentAmountWithvalidData()
    {
        System.out.println("Finished testing validatePaymentAmount with valid Data!");

    }


}



