package com.track_it;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.track_it.Presentation.SubscriptionInput;

public class SubscriptionInputTest {

    private SubscriptionInput subscriptionInput;
    private EditText inputLocation;
    private TextView errorMessageLocation;

    @Before
    public void setUp() {
        subscriptionInput = new SubscriptionInput();
        inputLocation = new EditText(null); // Use null as the context parameter
        errorMessageLocation = new TextView(null); // Use null as the context parameter
    }

    @Test
    public void testGetPaymentAmountInput_ValidInput() {
        // Set the input value
        inputLocation.setText("100.50");

        // Call the method under test
        int paymentAmount = subscriptionInput.getPaymentAmountInput(inputLocation, errorMessageLocation);

        // Verify the results...
        assertEquals(10050, paymentAmount);
        assertEquals("", errorMessageLocation.getText().toString());
        assertEquals(View.INVISIBLE, errorMessageLocation.getVisibility());
    }

    @Test
    public void testGetPaymentAmountInput_InvalidInput() {
        // Set the input value
        inputLocation.setText("abc");

        // Call the method under test
        int paymentAmount = subscriptionInput.getPaymentAmountInput(inputLocation, errorMessageLocation);

        // Verify the results...
        assertEquals(Integer.MIN_VALUE, paymentAmount);
        assertEquals("Invalid input for Payment amount", errorMessageLocation.getText().toString());
        assertEquals(View.VISIBLE, errorMessageLocation.getVisibility());
    }

    // Other test cases...

}


