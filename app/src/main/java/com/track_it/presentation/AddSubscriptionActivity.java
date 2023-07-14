package com.track_it.presentation;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.text.InputFilter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.track_it.R;
import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.exceptions.DataBaseException;
import com.track_it.logic.exceptions.SubscriptionException;
import com.track_it.presentation.util.DecimalDigitsInputFilter;
import com.track_it.presentation.util.FrequencyMenu;
import com.track_it.presentation.util.SetupParameters;
import com.track_it.presentation.util.SubscriptionInput;


// This class handles the presentation of the add subscription page for the app.
public class AddSubscriptionActivity extends AppCompatActivity {

    private String accomplishColor = "#8c1f7c";

    private static final String successAddMessage = "Subscription Added!";  // Message to display if add sub was successful

    private int MAX_DIGITS_BEFORE_DECIMAL; // The maximum number of digits before the decimal for payment amount
    private final int MAX_PAYMENT_DECIMALS = 2; // The maximum number of digits after the decimal for payment amount
    private SubscriptionHandler subHandler; // Will hold the AddSubscriptionHandler
    private  EditText nameInput; // Input target for the name of the subscription
    private EditText paymentAmount; // Target for input of payment amount
   private  Button addSubtarget; // To target add subscription button
    private Button backTarget; // To target back button
   private TextView generalErrorTarget; // where general error messages are displayed

    private  TextView paymentAmountError;
    private TextView nameError;
    private TextView frequencyError;




    private boolean successTry; // used by the clickedAddSubscriptionButton function, to keep track of if all the input is valid


    private AutoCompleteTextView frequencyTarget;
    private TextInputLayout dropDownMenuParent;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);

        subHandler = SetupParameters.getSubscriptionHandler();

        setTargets(); //Set global variable targets
        constrainUserInput(); //Set what a user can enter for input
        FrequencyMenu.initializeMenu(this, subHandler, frequencyTarget); // Enable drop down menu
        setButtonActions(); //Set what happens when buttons are clicked
    }






    //Constrain what a user can enter for input
    private void constrainUserInput()
    {

        // This physically constrains the user for what they can enter into the payment amount field ( How many digits before decimal, how many after)
        MAX_DIGITS_BEFORE_DECIMAL = SubscriptionInput.NumDigits(subHandler.getMaxPaymentDollars()); // get the number of digits allowed before decimal (used to constrain user input)
        paymentAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(MAX_PAYMENT_DECIMALS, MAX_DIGITS_BEFORE_DECIMAL)}); // Pass setFilters and array of objects that implement the InputFilter interface

        //limit what the user can enter for name
        int maxLength = subHandler.getMaxNameLength();
        nameInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)}); // Set max length the user can enter for input


    }


    //Set the global variable targets
    private void setTargets()
    {
        // Set the add subscription button click handler (What runs when the add subscription button is click)
        addSubtarget = (Button) findViewById(R.id.submit_sub_button);

        //Frequency menu targets
        frequencyTarget = findViewById(R.id.AutoComplete_drop_menu);
        dropDownMenuParent = findViewById(R.id.parent_drop_menu);

        //Input targets for payment and name
        paymentAmount = findViewById(R.id.input_payment_amount);  // Target Payment amount input
        nameInput = (EditText) findViewById(R.id.input_subscription_name); // Set target for name input


        // Set where  error messages are displayed
        generalErrorTarget = ((TextView) findViewById(R.id.subscription_error));
        paymentAmountError = ((TextView) findViewById(R.id.input_payment_amount_error));
        nameError = ((TextView) findViewById(R.id.input_subscription_name_error)); // where to display name errors
        frequencyError = ((TextView) findViewById(R.id.input_frequency_error )); // where to display name errors



    }


   //  Set What happens when buttons are clicked
    private void setButtonActions()
    {


        //Set what happens when add button clicked
        addSubtarget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                clickedAddSubscriptionButton(v); // Run this function when the user clicks the add subscription button.
            }
        });



        // Set back button target
        backTarget = (Button) findViewById(R.id.go_home);

        // Set what happens when backButton clicked
        backTarget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setContentView(R.layout.activity_main); // Switch screen to display main page
                finish();

            }
        });

    }





    // What to run when the user clicks the add Subscription button
    private void clickedAddSubscriptionButton(View view) {

        successTry = true; // Is all the input valid? (this will become false if anything is detected as being invalid)

        String userNameInput = getNameInput(view); // Get input for name,

        // Get input for payment amount
         SubscriptionInput subInput = new SubscriptionInput(subHandler); // Make a helper object, to get user input
          int paymentInCents = 1;
          try {
              paymentInCents = subInput.getPaymentAmountInput(paymentAmount);
              paymentAmountError.setVisibility(View.INVISIBLE);

          }
          catch(Exception e)
          {
              successTry = false;
              paymentAmountError.setText(e.getMessage());
              paymentAmountError.setVisibility(View.VISIBLE);
          }


        String PaymentFrequency = getPaymentFrequency(view); //Get input for payment frequency


        if (successTry )  // Only if all of our internal checks have passed, try to add subscription to database
        {
            // Create a new Subscription object
            SubscriptionObj newSubscription = new SubscriptionObj(userNameInput, paymentInCents, PaymentFrequency); // Sets the parameters

            try { // Try to add subscription to dataBase

                subHandler.addSubscription(newSubscription); //Throws an error if could not add subscription to database
                successAddedSubscription();

            }
            // Something went wrong, display error for user
            catch (SubscriptionException | DataBaseException e) {
                generalErrorTarget.setText(e.getMessage());
                generalErrorTarget.setVisibility(view.VISIBLE);
                successTry = false;
            }

        }
        else // Else our internal checks did not pass
        {
            generalErrorTarget.setText("Invalid Input");
            generalErrorTarget.setVisibility(view.VISIBLE);
        }

    }


    //What runs if a subscription was successfully added to the database,
    // Will set some success messages, show a toast message, and switch view back to main, and then finish this activity
    private void  successAddedSubscription()
    {
        generalErrorTarget.setVisibility(View.VISIBLE);
        generalErrorTarget.setText(successAddMessage);
        generalErrorTarget.setTextColor(Color.parseColor(accomplishColor));
        disableAddSubscriptionsButtons();

        Toast.makeText(this, successAddMessage, Toast.LENGTH_SHORT).show(); //Display "Subscription Added"
        setContentView(R.layout.activity_main); // Switch screen to display main page
        finish(); //We are done with this activity

    }




    // Get the name input from the user.
    // This will throw an Exceptions if name input is invalid, display the error message for the users, and set successTry to false
    private String getNameInput(View view) throws SubscriptionException
    {
        // Get the string the user entered for a name
        String userNameInput = nameInput.getText().toString().trim(); // get string, and remove white spaces

        try {
            subHandler.validateName(userNameInput);
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
    private String getPaymentFrequency(View view)
    {

        String PaymentFrequency = frequencyTarget.getText().toString(); // Get payment frequency from user input

        // Try to validate selection, if Exception is detected display error to user
        try {
            subHandler.validateFrequency(PaymentFrequency);
              frequencyError.setVisibility(view.INVISIBLE);


        } catch (SubscriptionException e) {
            frequencyError.setText(e.getMessage());
            frequencyError.setVisibility(view.VISIBLE);
            successTry = false;
        }


        return PaymentFrequency;

    }



    // Disable add sub Button and input after we added the sub
    private void disableAddSubscriptionsButtons()
    {
         addSubtarget.setEnabled(false); // Disable the add button

        // Make all the input uneditable
        nameInput.setEnabled(false);

        paymentAmount.setEnabled(false);


    }

}


