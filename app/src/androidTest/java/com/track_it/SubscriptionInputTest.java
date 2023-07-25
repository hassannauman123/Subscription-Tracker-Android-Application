package com.track_it;
import org.junit.Test;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import androidx.test.platform.app.InstrumentationRegistry;
import android.content.Context;

import android.widget.EditText;
import android.widget.TextView;

import com.track_it.Presentation.SubscriptionInput;

@RunWith(AndroidJUnit4.class)
public class SubscriptionInputTest {
    // Get the application context
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void testGetPaymentAmountInput() {
        // Run the test on the main UI thread
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                // Create input EditText and error TextView
                EditText inputLocation = new EditText(appContext);
                TextView errorMessageLocation = new TextView(appContext);

                // Set the input value
                inputLocation.setText("10.50");

                // Create an instance of SubscriptionInput
                SubscriptionInput subscriptionInput = new SubscriptionInput();

                // Call the method to be tested
                int result = subscriptionInput.getPaymentAmountInput(inputLocation, errorMessageLocation);

                // Assert that the result matches the expected value
                assertEquals(1050, result);
            }
        });
    }
}





