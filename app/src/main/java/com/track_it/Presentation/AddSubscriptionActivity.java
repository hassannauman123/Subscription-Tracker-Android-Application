package com.track_it.Presentation;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.InputFilter;
import android.widget.RadioButton;
import android.widget.TextView;
import com.track_it.R;
import com.track_it.domainObject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.exception.*;
import com.track_it.logic.exception.SubscriptionException;


// This class handles the presentation of the subscription page for the android app.


public class AddSubscriptionActivity extends AppCompatActivity {

    private String accomplishColor = "#8c1f7c";

    private final int MAX_DIGITAL_BEFORE_DECIMAL = SubscriptionHandler.getMaxPaymentDigitsBeforeDecimal(); // The maximum number of digits (before the decimal point) that can be entered by user for payment amount
    private final int MAX_PAYMENT_DECIMALS = 2; // The maximum number of digits after the decimal for payment amount
    private SubscriptionHandler handler; // Will hold the AddSubscriptionHandler

    Button addSubtarget; // To target add subscription
    Button backTarget; // To target back button
   TextView generalErrorTarget; // where general error messages are display
    private boolean successTry; // used by the clickedAddSubscriptionButton function, to keep track of if all the input is valid

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subscription);


        generalErrorTarget =((TextView) findViewById(R.id.subscription_error)); // Set where general error messages are displayed
        handler = new SubscriptionHandler(); // Set up subscription handler object


        // Set the add subscription button click handler (What runs when the add subscription button is click)
        addSubtarget= (Button) findViewById(R.id.submit_sub_button);
        addSubtarget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                clickedAddSubscriptionButton(v); // Run this function when the user clicks the add subscription button.

            }
        });


        // Set what happens when back button clicked ( ie, go home
        backTarget = (Button) findViewById(R.id.go_home);
        backTarget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setContentView(R.layout.activity_main); // Switch screen to display main page
                finish();

            }
        });



        // This physically constrains the user for what they can enter into the payment amount field ( How many digits before decimal, how many after)
        EditText etText = findViewById(R.id.input_payment_amount);  // Target Payment amount input
        etText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(MAX_PAYMENT_DECIMALS, MAX_DIGITAL_BEFORE_DECIMAL)}); // Pass setFilters and array of objects that implement the InputFilter interface
    }



    // What to run when the user clicks the add Subscription button
    private void clickedAddSubscriptionButton(View view) {

         successTry = true; // Is all the input valid? (this will become false if anything wrong is detected)


        String userNameInput = getNameInput( view); // Get input for name,

        // Get input for payment amount
         SubscriptionInput subInput = new SubscriptionInput(); // Make a helper object, to get user input
          int paymentInCents = 1;
          try {
              subInput.getPaymentAmountInput((EditText) findViewById(R.id.input_payment_amount));
              ((TextView) findViewById(R.id.input_payment_amount_error)).setVisibility(View.INVISIBLE);

          }
          catch(Exception e)
          {
              successTry = false;
              ((TextView) findViewById(R.id.input_payment_amount_error)).setText(e.getMessage());
              ((TextView) findViewById(R.id.input_payment_amount_error)).setVisibility(View.VISIBLE);
          }



        String PaymentFrequency = getPaymentFrequency(view); //Get input for payment frequency


        if (successTry )  // Only if all of our internal checks have passed, try to add subscription to database
        {
            // Create a new Subscription object
            SubscriptionObj newSubscription = new SubscriptionObj(userNameInput, paymentInCents, PaymentFrequency); // Sets the parameters

            try { // Try to add subscription to dataBase

                handler.addSubscription(newSubscription);
                generalErrorTarget.setVisibility(View.VISIBLE);
                generalErrorTarget.setText("Subscription successfully Added!");
                generalErrorTarget.setTextColor(Color.parseColor(accomplishColor));

               disableAddSubscriptionsButtons();

            }
            // Something went wrong, display error for user
            catch (SubscriptionException e) {
                generalErrorTarget.setText(e.getMessage());
                generalErrorTarget.setVisibility(view.VISIBLE);
                successTry = false;
            }
        }
        else // Else our internal checks did not pass
        {
            generalErrorTarget.setText("Error with input");
            generalErrorTarget.setVisibility(view.VISIBLE);
        }

    }




    // Get the name input from the user.
    // This will throw an Exceptions if name input is invalid, display the error message for the users, and set successTry to false
    private String getNameInput(View view) throws SubscriptionException
    {
        // Get the string the user entered for a name
        EditText textInput;
        textInput = (EditText) findViewById(R.id.input_subscription_name);
        String userNameInput = textInput.getText().toString().trim(); // get string, and remove white spaces
        TextView nameError = ((TextView) findViewById(R.id.input_subscription_name_error)); // where to display name errors

        try {
            handler.validateName(userNameInput);
            nameError.setText("");
            nameError.setVisibility(View.INVISIBLE);


        } catch (SubscriptionException e) {
            successTry = false;
            nameError.setText(e.getMessage());
            nameError.setVisibility(View.VISIBLE);
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
        TextView frequencyError = ((TextView) findViewById(R.id.input_frequency_group_error )); // where to display name errors


        // Target all the radio buttons for Payment frequency
        RadioButton radioWeek, RadioMonth, radioYear;
        radioWeek = (RadioButton) findViewById(R.id.input_frequency_weekly);
        RadioMonth = (RadioButton) findViewById(R.id.input_frequency_monthly);
        radioYear = (RadioButton) findViewById(R.id.input_frequency_yearly);

        // Find which radio button is selected
        if (radioWeek.isChecked()) {
            PaymentFrequency = SubscriptionHandler.FREQUENCY.weekly.toString();
        } else if (RadioMonth.isChecked()) {
            PaymentFrequency = SubscriptionHandler.FREQUENCY.monthly.toString();;
        } else if (radioYear.isChecked()) {
            PaymentFrequency = SubscriptionHandler.FREQUENCY.yearly.toString();
        }


        // Try to validate selection, if Exception is detected display error to user
        try {
            handler.validateFrequency(PaymentFrequency);
              frequencyError.setVisibility(view.INVISIBLE);


        } catch (SubscriptionException e) {
            frequencyError.setText(e.getMessage());
            frequencyError.setVisibility(view.VISIBLE);
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


    // Disable add Button after we added the sub
    private void disableAddSubscriptionsButtons()
    {
         addSubtarget.setEnabled(false); // Disable the add button


        // Make all the input uneditable
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


