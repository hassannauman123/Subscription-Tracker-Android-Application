package com.track_it;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.track_it.logic.SubscriptionHandler;
import com.track_it.persistence.fakes.FakeSubscriptionPersistenceDatabase;
import com.track_it.presentation.util.SetupParameters;
import com.track_it.util.TestUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class SubscriptionValidateTest {


    private SubscriptionHandler subHandle;


    //Setup a fake database each time
    @Before
    public void setTestHandle()
    {

        TestUtils.changeDatabase();
         subHandle =  SetupParameters.getSubscriptionHandler();

    }

    @Test
    // We are going to test the validate name test
    public void testNameValidateWithInValidData() {


        boolean thrown = false;
        String inputName = "";
        try {
            subHandle.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("An empty string should not be valid input", thrown);

        // Invalid characters
        thrown = false;
        inputName = "One~Twothree` ";

        try {
            subHandle.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);

        // Try lots of blank space
        for (int i = 0; i < 100; i++) {
            inputName = inputName + " ";
            thrown = false;
            try {
                subHandle.validateName(inputName);
            } catch (Exception e) {
                thrown = true;
            }
            assertTrue("Multiple spaces for a subscription name should not be valid input", thrown);
        }

        // Try white spaces first
        thrown = false;
        inputName = " onettwo";

        try {
            subHandle.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("Should not allow white spaces at the start of a sub name", thrown);


        // Try white spaces trailing
        thrown = false;
        inputName = "onettwo ";

        try {
            subHandle.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("Should not allow white spaces at the end of a sub name", thrown);

        // Invalid characters
        thrown = false;
        inputName = "One~Twothree` ";

        try {
            subHandle.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("Should not allow invalid characters in the sub name", thrown);

        System.out.println("PASSED testing validateName with invalid Data!");
    }

    @Test
    // We are going to test the validateName function with valid names
    public void testNameValidateWithValidData() {

        // Valid 3 letter name
        boolean thrown = false;
        String inputName = "One";

        try {
            subHandle.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("'One' was incorrectly flagged as an invalid sub name", !thrown);


        // Valid name with alpha characters
        thrown = false;
        inputName = "On3ee3";

        try {
            subHandle.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("A sub name with valid characters was incorrectly flagged as invalid", !thrown);


        // Valid  name with spaces one space in middle
        thrown = false;
        inputName = "One Two";

        try {
            subHandle.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("A sub name with spaces in the middle was incorrectly flagged as invalid input", !thrown);


        // Valid  name with two spaces in middle
        thrown = false;
        inputName = "One  Two";

        try {
            subHandle.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("A sub name with multiple spaces in the middle was incorrectly flagged as invalid input", !thrown);


        // Valid 100 Char name
        thrown = false;
        inputName = "a23";
        String allowChars = subHandle.getAllowableChars();


        for (int i = 0; i < subHandle.getMaxNameLength() - 3; i++) {
            char newChar = allowChars.charAt((int) (Math.random() * allowChars.length()));

            while (newChar == ' ') // The last letter of  name cannot be a space!!!
            {
                newChar = allowChars.charAt((int) (Math.random() * allowChars.length()));
            }

            inputName = inputName + newChar;
            try {
                subHandle.validateName(inputName);
            } catch (Exception e) {
                System.out.println("Name:  " + inputName + " was invalid.  Thrown on index " + i);
                System.out.println(e.getMessage());
                thrown = true;
            }
            assertTrue("A name consisting of multiple valid characters was incorrectly flagged as invalid input", !thrown);

            // Try to short letters
            thrown = false;
            inputName = "ab";

            try {
                subHandle.validateName(inputName);
            } catch (Exception e) {
                thrown = true;
            }
            assertFalse("A string consisting of less than 3 characters should not be invalid input", thrown);


            // Try non alpha letters first
            thrown = false;
            inputName = "1ab";

            try {
                subHandle.validateName(inputName);
            } catch (Exception e) {
                thrown = true;
            }
            assertFalse("Sub name can start with a number", thrown);
        }


        // Try to short letters
        thrown = false;
        inputName = "ab";

        try {
            subHandle.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertFalse(thrown);


        // Try non alpha letters first
        thrown = false;
        inputName = "1ab";

        try {
            subHandle.validateName(inputName);
        } catch (Exception e) {
            thrown = true;
        }
        assertFalse(thrown);

        System.out.println("PASSED testing validateName with valid Data!");
    }

    // Test Validate payment frequency with invalid Data
    @Test
    public void testValidatePaymentFrequencyWithInValidData() {


        boolean thrown = false;
        String inputFrequency = "";


        // Invalid data with empty string
        thrown = false;
        inputFrequency = "";

        try {
            subHandle.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("Empty string was incorrectly accepted as valid payment frequency", thrown);

        // Invalid data with blank space
        thrown = false;
        inputFrequency = "  ";

        try {
            subHandle.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("Multiple blank spaces were incorrectly accepted as valid payment frequency", thrown);

        // Invalid data with strange looking input
        thrown = false;
        inputFrequency = " one two";

        try {
            subHandle.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }

        assertTrue("' one two' was incorrectly accepted as valid payment frequency", thrown);


        // Invalid data with correct frequency + char
        thrown = false;
        inputFrequency = subHandle.getFrequencyNameList().get(0) +  "1";

        try {
            subHandle.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("'"+ subHandle.getFrequencyNameList().get(0) + "1' was incorrectly accepted as valid payment frequency", thrown);




        // Invalid data with correct frequency + blank character
        thrown = false;
        inputFrequency = subHandle.getFrequencyNameList().get(1) + " ";

        try {
            subHandle.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("'"+ subHandle.getFrequencyNameList().get(1) + " ' was incorrectly accepted as valid payment frequency", thrown);


        // Invalid data with correct frequency + blank character
        thrown = false;
        inputFrequency = " " + subHandle.getFrequencyNameList().get(1);

        try {
            subHandle.validateFrequency(inputFrequency);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("' "+ subHandle.getFrequencyNameList().get(1) + "' was incorrectly accepted as valid payment frequency", thrown);


        System.out.println("PASSED testing validatePaymentFrequency with invalid Data!");


    }


    // Test Validate payment frequency with invalid Data
    @Test
    public void testValidatePaymentFrequencyWithValidData() {

        boolean thrown = false;
        String inputFrequency = "";


        // Invalid data with correct frequency + char
        thrown = false;
        List<String> allowableFrequency= subHandle.getFrequencyNameList();

        for (int i = 0; i < allowableFrequency.size(); i++)
        {
            thrown = false;
            inputFrequency = allowableFrequency.get(i);
            try {
                subHandle.validateFrequency(inputFrequency);

            }
            catch ( Exception e)
            {
                System.out.println(e.getMessage());
                thrown = true;
            }

            assertTrue("validate frequency Test failed with input " + inputFrequency,!thrown);
        }



        System.out.println("PASSED testing validatePaymentFrequency with valid Data!");


    }


    // Test Validate paymentAmount with invalid Data
    @Test
    public void testValidatePaymentAmountWithInvalidData() {

        int paymentAmount = 0;
        boolean thrown = false;


        // Payment too small
        thrown = false;
        paymentAmount = 0;
        try {
            subHandle.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("Payment amount should not be 0", thrown);


        // Payment too small
        thrown = false;
        paymentAmount = -1;
        try {
            subHandle.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("Payment amount should not be a negative number", thrown);


        // Payment too small
        thrown = false;
        paymentAmount = Integer.MIN_VALUE;
        try {
            subHandle.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("Payment amount should not be a negative number", thrown);

        // Payment too large
        thrown = false;
        paymentAmount =  Integer.MAX_VALUE;
        try {
            subHandle.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(Integer.MAX_VALUE + " should not be considered a valid payment amount", thrown);

        // Payment too large
        thrown = false;
        paymentAmount =  subHandle.getMaxPaymentCentsTotal() + 1;
        try {
            subHandle.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("Payment amount should not exceed " + subHandle.getMaxPaymentCentsTotal(), thrown);

        System.out.println("PASSED testing validatePaymentAmount with invalid Data!");




        // Payment should be too large by exactly 1 cent!
        thrown = false;
        paymentAmount =  9999;// should be like 9999
        paymentAmount = paymentAmount * 100;
        paymentAmount += 100;
        try {
            subHandle.validatePaymentAmount(paymentAmount);

        } catch (Exception e) {
            thrown = true;
        }
        assertTrue( "PaymentAmount " + paymentAmount + " was said to be valid, when it was suppose to be invalid", thrown);



        System.out.println("PASSED testing validatePaymentAmount with invalid Data!");

    }


    // Test Validate payment frequency with valid Data
    @Test
    public void testValidatePaymentAmountWithvalidData() {


        int paymentAmount = 0;
        boolean thrown = false;


        // Payment correct
        thrown = false;
        paymentAmount = 1;
        try {
            subHandle.validatePaymentAmount(paymentAmount);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue("1 should have been considered a valid payment amount", !thrown);


        int maxPayment = subHandle.getMaxPaymentCentsTotal();
        //Cycle through all correct payments
       for ( int i = 1 ; i <=maxPayment; i++ )
       {

           thrown = false;
           paymentAmount= i;
           try {
               subHandle.validatePaymentAmount(paymentAmount);
           } catch (Exception e) {

               System.out.println("Validate payment amount failed with input "+ i);
               thrown = true;
           }

           assertFalse(i + "should have been considered a valid payment amount", thrown);

       }


        // Payment should be valid (The exact max payment allowed)
        thrown = false;
        paymentAmount =  9999; // should be like 9999
        paymentAmount = paymentAmount * 100;
        paymentAmount += 99;
        try {
            subHandle.validatePaymentAmount(paymentAmount);

        } catch (Exception e) {
            thrown = true;
        }
        assertFalse(   "PaymentAmount " + paymentAmount + " was said to be invalid, when it was suppose to be valid", thrown);

        System.out.println("PASSED testing validatePaymentAmount with valid Data!");

    }


}



