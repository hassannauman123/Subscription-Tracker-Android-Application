package com.track_it.Presentation;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.track_it.R;
import com.track_it.domainObject.SubscriptionObj;
import com.track_it.logic.exception.SubscriptionException;
import com.track_it.logic.SubscriptionHandler;

public class SubscriptionDetailsActivity extends AppCompatActivity {


    private int MAX_PAYMENT_DECIMALS = 2;
    private int MAX_DIGITAL_BEFORE_DECIMAL = SubscriptionHandler.getMaxPaymentDigitsBeforeDecimal();
    private boolean alreadyDeleted = false;

    private boolean editMode = false;
    private String errorColor = "#ff0000"; // I think we are suppose to put this is some shared resource file
    private String accomplishColor = "#8c1f7c";
    private String saveButtonColor = "#57f2a0";
    private String editButtonColor = "#6632a8";


    private EditText nameTarget;
    private EditText paymentAmountTarget;
    private EditText frequencyTarget;
    private TextView generalErrorTarget;

    private SubscriptionObj subscriptionToDisplay;
    private Button editButton;
    private Button backButton;

    private static final String validEditMessage = "Subscription Edited!";
    private static final String successDeleteMessage = "Subscription Deleted!";
    private static final String alreadyDeletedMessage = "Subscription has already been Deleted!";

    private final String confirmationMsg = "Are you sure you want to delete the subscription?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_details);

        nameTarget = ((EditText) findViewById(R.id.detail_subscription_name));
        paymentAmountTarget = ((EditText) findViewById(R.id.detail_subscription_amount));
        frequencyTarget = ((EditText) findViewById(R.id.detail_subscription_frequency));

        backButton = (Button) findViewById(R.id.go_home);
        generalErrorTarget = ((TextView) findViewById(R.id.details_general_error));
        editButton = (Button) findViewById(R.id.details_edit_subscription);

        SubscriptionHandler subGetHandler = new SubscriptionHandler();

        setFocus(false); // Disable editing sub details


        //Enable go back button
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                finish();
            }
        });

        // Try to get the input passed to this activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int subscriptionID = extras.getInt("subscriptionID");

            try {
                subscriptionToDisplay = subGetHandler.getSubscriptionByID(subscriptionID);
                setDetails(subscriptionToDisplay);

                //Enable delete button
                Button button = (Button) findViewById(R.id.details_delete_subscription);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if ( !editMode)  // If we are not in edit mode, let the user delete it
                        {

                            createDialog(subscriptionToDisplay.getID());
                        }

                    }
                });


                //Run this when editButton is clicked
                editButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        editButton();

                    }
                }); //

            } catch (Exception e) {
                setGeneralError(e.getMessage(), errorColor);
            }
        }
        else  // We we display if we could not get passed input
        {
            setGeneralError("Invalid Selection for subscription", errorColor);
        }

        // This physically constrains the user for what they can enter into the payment amount field ( How many digits before decimal, how many after)
        EditText etText = findViewById(R.id.detail_subscription_amount);  // Target Payment amount input
        etText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(MAX_PAYMENT_DECIMALS, MAX_DIGITAL_BEFORE_DECIMAL)}); // Pass setFilters and array

    }

    // This changes whether the input can be edited by the user.
    // inputBool = true -> makes it editable
    // inputBool = false -> makes it not editable
    private void setFocus(boolean inputBool) {
        nameTarget.setEnabled(inputBool);
        paymentAmountTarget.setEnabled(inputBool);
        frequencyTarget.setEnabled(inputBool);


        String color2 = "#222222"; // Working fast
        if (inputBool) {
            nameTarget.setTextColor(Color.parseColor(color2));
            paymentAmountTarget.setTextColor(Color.parseColor(color2));
            frequencyTarget.setTextColor(Color.parseColor(color2));
        } else {
            nameTarget.setTextColor(Color.parseColor("#000000"));
            paymentAmountTarget.setTextColor(Color.parseColor("#000000"));
            frequencyTarget.setTextColor(Color.parseColor("#000000"));
        }

    }

    // Toggle the edit sub button from edit to save changes (or vice versa)
    private void changeEditButton() {
        if (editMode == false) {
            editButton.setBackgroundColor(Color.parseColor(editButtonColor));
            editButton.setText("Edit Subscription");

        } else {
            editButton.setBackgroundColor(Color.parseColor(saveButtonColor));
            editButton.setText("Save Changes");
        }
    }

    // This runs when a user clicks the edit button
    private void editButton() {
        //If we are not in edit mode, and the subscription has not already been deleted  -then switch to edit mode
        if (editMode == false && !alreadyDeleted) {

            editMode = true; // change to edit mode
            setFocus(true); // enable the inputs to be edited
            changeEditButton(); // toggle the edit button to change

        }
        else if ( alreadyDeleted)
        {
            setGeneralError("Cannot edit subscription:\n Subscription has been deleted!", errorColor);

        }

        else // Else we are already in edit mode, and user is try to save changes.
        {

            boolean valid = true;// Tells us if everything the user entered is valid
            SubscriptionHandler handler = new SubscriptionHandler();// users to get payment amount


            // Get the frequency the user entered
            String inputFrequency = frequencyTarget.getText().toString().trim();
            try {

                handler.validateFrequency(inputFrequency);

            } catch (SubscriptionException e)  //Catch - Frequency was not valid
            {

                // Display error
                valid = false;
               setGeneralError(e.getMessage(), errorColor);
                generalErrorTarget.setVisibility(View.VISIBLE);

            }


            // Get the payment amount the user Enter
            SubscriptionInput subInput = new SubscriptionInput();
            int inputPaymentAmount = 0;

            try {
                inputPaymentAmount = subInput.getPaymentAmountInput(paymentAmountTarget); // This function throws exceptions, and sets user error message accordingly
            }
            catch(SubscriptionException e) // Payment amount not valid
            {
                // Display errors
                valid = false;
                setGeneralError(e.getMessage(), errorColor);
                generalErrorTarget.setVisibility(View.VISIBLE);
            }


            // Get the string the user entered for a name
            String userNameInput = nameTarget.getText().toString().trim(); // get string, and remove white spaces
            try {
                handler.validateName(userNameInput); // Check if value is valid

            } catch (SubscriptionException e) // Catch - name was not valid,
            {
                // Display errors
                valid = false;
                setGeneralError(e.getMessage(), errorColor);
                generalErrorTarget.setVisibility(View.VISIBLE);

            }

            if ( valid )
            {
                generalErrorTarget.setVisibility(View.INVISIBLE);

            }


            // If everything was detected as valid, try to change the info
            if (valid) {
                try {
                    SubscriptionHandler logicHandler = new SubscriptionHandler();

                    subscriptionToDisplay.setName(userNameInput);
                    subscriptionToDisplay.setPaymentFrequency(inputFrequency);
                    subscriptionToDisplay.setPayment(inputPaymentAmount);
                    logicHandler.editWholeSubscription(subscriptionToDisplay.getID(), subscriptionToDisplay);

                    //Everything worked, so time to switch back to none-edit mode!
                    editMode = false;
                    setFocus(false); // No longer allow inputs to be edited
                    changeEditButton(); // Edit button changes back
                    setGeneralError(validEditMessage, accomplishColor);
                } catch (Exception e) {
                    setGeneralError(e.getMessage(), errorColor);
                    generalErrorTarget.setVisibility(View.VISIBLE);

                }

            }


        }
    }


    //PopupMessage Confirming to our user if they really want to delete a subscription
    private void createDialog(int subscriptionToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SubscriptionDetailsActivity.this);
        builder.setTitle(confirmationMsg);

        // Make a Yes button, meaning that the user does in fact want to delete the subscription
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SubscriptionHandler SubHandler = new SubscriptionHandler();
                try {

                    SubHandler.removeSubscriptionByID(subscriptionToDelete); // Attempt to delete the sub
                    alreadyDeleted = true;
                    setGeneralError(successDeleteMessage, accomplishColor);
                } catch (Exception e) {
                    if (!alreadyDeleted) {
                        setGeneralError(e.getMessage(), errorColor); // Some  unkown error
                    } else {
                        setGeneralError(alreadyDeletedMessage, errorColor); // We already deleted it
                    }


                }
            }
        });
        // Make a No button
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // We do not need to do anything, as we won't delete subscription
            }
        });
        AlertDialog dialog = builder.create(); // create alert
        dialog.show(); // show it

    }

    private void setGeneralError(String inputMessage, String inputColor) {
        generalErrorTarget.setTextColor(Color.parseColor(inputColor));
        generalErrorTarget.setText(inputMessage);
        generalErrorTarget.setVisibility(View.VISIBLE);

    }

    private void setDetails(SubscriptionObj subscriptionToDisplay) {
        setName(subscriptionToDisplay.getName());
        setPaymentAmount(subscriptionToDisplay.getPaymentDollars() + "." + subscriptionToDisplay.getPaymentCents());
        setPaymentFrequency(subscriptionToDisplay.getPaymentFrequency());
    }

    private void setName(String nameToDisplay) {
        ((TextView) findViewById(R.id.detail_subscription_name)).setText(nameToDisplay);
    }

    private void setPaymentAmount(String paymentToDisplay) {
        ((TextView) findViewById(R.id.detail_subscription_amount)).setText(paymentToDisplay);
    }

    private void setPaymentFrequency(String frequencyToDisplay) {
        ((TextView) findViewById(R.id.detail_subscription_frequency)).setText(frequencyToDisplay);
    }

}





