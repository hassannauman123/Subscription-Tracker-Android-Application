package com.track_it.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.track_it.R;
import com.track_it.domainObject.SubscriptionObj;
import com.track_it.logic.AddSubscriptionHandler;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.exception.*;

import java.util.concurrent.Flow;


// This class handles the presentation of the subscription page for the android app.
//
//

public class AddSubscriptionActivity extends AppCompatActivity {

    private final int MAX_DIGITAL_BEFORE_DECIMAL = SubscriptionHandler.getMaxPaymentDigitsBeforeDecimal(); // The maximum number of digits (before the decimal point) that can be entered by user for payment amount
    private final int MAX_PAYMENT_DECIMALS = 2; // The maximum number digits after the decimal for payment amount

    private AddSubscriptionHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subscription);


        handler = new AddSubscriptionHandler();


        // Set the add subscription button click handler (What runs when the add subscription button is click)
        Button button = (Button) findViewById(R.id.submit_sub_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                clickedAddSubscriptionuttoButton(v); // Run this function when the user clicks the add subscription button.

            }
        });


        // Go back to main page when go back is clicked
        button = (Button) findViewById(R.id.go_home);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setContentView(R.layout.activity_main); // Switch screen to display main page
                finish();

            }
        });



        // This physically constrains the user for what they can enter into the payment amount field ( How many digits before decimal, how many after)
        EditText etText = findViewById(R.id.input_payment_amount);  // Target Payment amount input
        etText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(MAX_PAYMENT_DECIMALS, MAX_DIGITAL_BEFORE_DECIMAL)}); // Pass setFilters and array of objects that implement the InputFilter interface
    }


    private boolean successTry = true;


    private void clickedAddSubscriptionuttoButton(View view) {

         successTry = true;


        String userNameInput = getNameInput( view);
        int paymentInCents = getPaymentAmount(view);
        String PaymentFrequency = getPaymentFrequency(view);




        if (successTry )  // Only if all of our internal checks have passed, try to add subscription to database
        {
            // Create a new Subscription object
            SubscriptionObj newSubscription = new SubscriptionObj(userNameInput, paymentInCents, PaymentFrequency);

            try { // Try to add subscription to dataBase

                handler.addSubscription(newSubscription);
                ((TextView) findViewById(R.id.subscription_error)).setVisibility(view.VISIBLE);
                ((TextView) findViewById(R.id.subscription_error)).setText("Subscription successfully Added!");
               ((TextView) findViewById(R.id.subscription_error)).setTextColor(Color.parseColor("#00FF00"));

               disableAddSubscriptionsButtons();

            } catch (SubscriptionException e) {
                ((TextView) findViewById(R.id.subscription_error)).setText(e.getMessage());
                ((TextView) findViewById(R.id.subscription_error)).setVisibility(view.VISIBLE);
            }
        }
        else {
            ((TextView) findViewById(R.id.subscription_error)).setText("Error with input");
            ((TextView) findViewById(R.id.subscription_error)).setVisibility(view.VISIBLE);
        }

    }

    private int getPaymentAmount(View view)
    {

        EditText textInput = (EditText) findViewById(R.id.input_payment_amount);
        String[] paymentAmountString = textInput.getText().toString().split("\\.");
        int paymentInCents = Integer.MIN_VALUE;
        // I thought parseInt threw an exception on invalid data??
        // Anyways, get Payment amount in cents


        if (paymentAmountString.length > 0) {

            if (isParsable(paymentAmountString[0])) {
                paymentInCents = Integer.parseInt(paymentAmountString[0]) * 100;
            }

            if (paymentAmountString.length > 1) {

                if (isParsable(paymentAmountString[1])) {

                    if (paymentInCents == Integer.MIN_VALUE)
                    {
                        paymentInCents = 0;
                    }

                    paymentInCents = paymentInCents + Integer.parseInt(paymentAmountString[1]);
                }
            }
        }

        if (paymentInCents == Integer.MIN_VALUE) {
            ((TextView) findViewById(R.id.input_payment_amount_error)).setText("Invalid input for Payment amount");
            ((TextView) findViewById(R.id.input_payment_amount_error)).setVisibility(view.VISIBLE);
            successTry = false;

        } else {
            try {
                handler.validatePaymentAmount(paymentInCents);
                ((TextView) findViewById(R.id.input_payment_amount_error)).setVisibility(view.INVISIBLE);



            } catch (SubscriptionException e) {
                ((TextView) findViewById(R.id.input_payment_amount_error)).setText(e.getMessage());
                ((TextView) findViewById(R.id.input_payment_amount_error)).setVisibility(view.VISIBLE);
                successTry = false;
            }

        }

        return paymentInCents;

    }

    private String getNameInput(View view)
    {
        // Get the string the user entered for a name
        EditText textInput;
        textInput = (EditText) findViewById(R.id.input_subscription_name);
        String userNameInput = textInput.getText().toString().trim(); // get string, and remove white spaces

        try {
            handler.validateName(userNameInput);
            ((TextView) findViewById(R.id.input_subscription_name_error)).setText("");
            ((TextView) findViewById(R.id.input_subscription_name_error)).setVisibility(view.INVISIBLE);


        } catch (SubscriptionException e) {
            successTry = false;
            ((TextView) findViewById(R.id.input_subscription_name_error)).setText(e.getMessage());
            ((TextView) findViewById(R.id.input_subscription_name_error)).setVisibility(view.VISIBLE);
        }

        return userNameInput;
    }


    // Get payment Frequency from user input
    // This function will automatically display any errors if detected with input, and set global variable successTry to false
    // Will return string representing what radio button user clicked for payment Frequency (may be empty if user selected nothing)
    private String getPaymentFrequency(View view)
    {
        //
        // Get payment Frequency from user input
        //
        String PaymentFrequency = ""; // This will stay blank if radio button not selected

        // Target all the radio buttons for Payment frequency
        RadioButton radioWeek, RadioMonth, radioYear;
        radioWeek = (RadioButton) findViewById(R.id.input_frequency_weekly);
        RadioMonth = (RadioButton) findViewById(R.id.input_frequency_monthly);
        radioYear = (RadioButton) findViewById(R.id.input_frequency_yearly);

        // Find which radio button is selected
        if (radioWeek.isChecked()) {
            PaymentFrequency = SubscriptionObj.WEEKLY;
        } else if (RadioMonth.isChecked()) {
            PaymentFrequency = SubscriptionObj.MONTHLY;
        } else if (radioYear.isChecked()) {
            PaymentFrequency = SubscriptionObj.YEARLY;
        }

        // Try to validate selection, if Exception is detected display error to user
        try {
            handler.validateFrequency(PaymentFrequency);
            ((TextView) findViewById(R.id.input_frequency_group_error)).setVisibility(view.INVISIBLE);


        } catch (SubscriptionException e) {
            ((TextView) findViewById(R.id.input_frequency_group_error)).setText(e.getMessage());
            ((TextView) findViewById(R.id.input_frequency_group_error)).setVisibility(view.VISIBLE);
            successTry = false;
        }

        return PaymentFrequency;

    }


    // check if the input string is parsable by Integer.parseInt function
    private boolean isParsable(String inputString) {
        try {
            Integer.parseInt(inputString);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }


    private void disableAddSubscriptionsButtons()
    {
        Button subButton = (Button) findViewById(R.id.submit_sub_button);
        subButton.setEnabled(false);

        EditText textInput = (EditText) findViewById(R.id.input_subscription_name);
        textInput.setEnabled(false);


        EditText paymentAmountTarget = (EditText) findViewById(R.id.input_payment_amount);
        paymentAmountTarget.setEnabled(false);

        RadioButton radioWeek, RadioMonth, radioYear;
        radioWeek = (RadioButton) findViewById(R.id.input_frequency_weekly);
        RadioMonth = (RadioButton) findViewById(R.id.input_frequency_monthly);
        radioYear = (RadioButton) findViewById(R.id.input_frequency_yearly);

        radioWeek.setEnabled(false);
        RadioMonth.setEnabled(false);
        radioYear.setEnabled(false);

    }

}



//Limits the user input for a decimal input to a format such that there will be a max of digitBeforeDecimal before decimal and a max of decimalDigits
class DecimalDigitsInputFilter implements InputFilter {

    private final int decimalDigits; // Maximum number of integer after decimal
    private final int digitBeforeDecimal; // Maximum number of integers before deciamls

    public DecimalDigitsInputFilter(int decimalDigits, int digitBeforeDecimal) {
        this.decimalDigits = decimalDigits;
        this.digitBeforeDecimal = digitBeforeDecimal;
    }


    // Makes it such that the string will be in the format such that there will at most be digitBeforeDecimal before the decimal, and decimalDigits after the decimal
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        //Reminder
        // source is the char being added.
        // destination is the previous string. Source will be added to destination at position dstart if we return null, else it will be reject if we return ""


        CharSequence returnValue = null; // Null means we accept source, "" means we reject it

        int dotPos = -1; // Where does the decimal occur in destination?
        int len = dest.length(); // Get length of previous string


        // Iterate over the string, looking for decimal character
        for (int i = 0; i < len; i++) {
            char charIterator = dest.charAt(i); // The current char being read

            if (charIterator == '.') {
                dotPos = i; // Decimal place found
                break;
            } else if ((i >= digitBeforeDecimal - 1) && !source.equals("."))  // The maximum amount of integers before the decimal

            {
                if ((i + 1) < len && dest.charAt(i + 1) == '.') // The next char after this is a decimal, so we are good
                {
                    continue; // Continue back to loop ( dotPos will end being = to i+1);
                } else {
                    returnValue = ""; // Else we hit the maximum number of integers before decimal, and the next char is not a decimal so don't add anything
                    break; // Break out of this for loop, and dotPos will <
                }
            }
        }

        if (dotPos >= 0)  // Decimal is in the string
        {

            // If source is being added to front of decimal and there is room accept source
            if (dend <= dotPos && (dotPos < digitBeforeDecimal)) {
                returnValue = null;
            }
            // Else - If there are already 2 digits past the decimal and source is going past decimal reject source
            else if (len - dotPos > decimalDigits) {
                returnValue = "";
            }
        }

        return returnValue; // Will be null if source accepted, and "" if source accepted!
    }

}
