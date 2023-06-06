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


//This is the activity java file for the description details.
// Allow the user to view details, delete subscription, and edit subscription.

public class SubscriptionDetailsActivity extends AppCompatActivity {


    private int MAX_PAYMENT_DECIMALS = 2;
    private int MAX_DIGITAL_BEFORE_DECIMAL = SubscriptionHandler.getMaxPaymentDigitsBeforeDecimal(); // Max payment amount in dollars (doesn't include cent count)
    private boolean alreadyDeleted = false; // Has the subscription been deleted during this session?
    private boolean editMode = false; // Are we in edit mode? Determines whether the input can be edited, and behaviour of the edit/save button

    // Various color - We should probably put this in some type of shared resource location later
    private String errorColor = "#ff0000"; //// error text color
    private String accomplishColor = "#8c1f7c"; // Accomplish text color
    private String saveButtonColor = "#57f2a0"; // what color the save button will be
    private String editButtonColor = "#6632a8";// what color the edit color button will be

    private String editableColor = "#555555"; // The color input will be when it is editable
    private String nonEditableColor = "#000000";  // The color input will be when it is uneditable


    // Various targets for buttons, and text regions
    private EditText nameTarget;
    private EditText paymentAmountTarget;
    private EditText frequencyTarget;
    private TextView generalErrorTarget; // Where error messages are shown to user on this page

    private SubscriptionObj subscriptionToDisplay; // The subscription we will display, and allow the user to edit or delete
    private Button editButton;
    private Button backButton;


    //String constants, messages to display to user
    private static final String validEditMessage = "Subscription Edited!"; // if edit was successful
    private static final String successDeleteMessage = "Subscription Deleted!";  // if delete was successful
    private static final String alreadyDeletedMessage = "Subscription has already been Deleted!"; // what to show if user tries to delete twice

    private final String confirmationMsg = "Are you sure you want to delete the subscription?"; // confirmation message for deleting sub


    // Handler for the subscriptions
    private SubscriptionHandler subHandler;

    private boolean loadSub = false; // Were we able to load the subscription object?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_details);

        // Set the various targets
        nameTarget = ((EditText) findViewById(R.id.detail_subscription_name));
        paymentAmountTarget = ((EditText) findViewById(R.id.detail_subscription_amount));
        frequencyTarget = ((EditText) findViewById(R.id.detail_subscription_frequency));

        backButton = (Button) findViewById(R.id.go_home);
        generalErrorTarget = ((TextView) findViewById(R.id.details_general_error));
        editButton = (Button) findViewById(R.id.details_edit_subscription);
        setFocus(false); // Disable editing sub details

        subHandler = new SubscriptionHandler(); // Init sub handler



        //Enable go back to home button
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Go back to home page
                setContentView(R.layout.activity_main);
                finish();
            }
        });

        // Try to get the subscription id input passed to this activity
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            int subscriptionID = extras.getInt("subscriptionID"); //Get subscriptionID

            try {
                subscriptionToDisplay = subHandler.getSubscriptionByID(subscriptionID); // Get the subscription object associated with subscriptionID

                setDetails(subscriptionToDisplay);

                loadSub = true;


            } catch (Exception e) // Failed to get subscription
            {
                setGeneralError(e.getMessage(), errorColor);
            }
        }
        else  // We we display if we could not get passed input
        {
            setGeneralError("Invalid Selection for subscription", errorColor);
        }


        if ( loadSub) // Make sure the subscription object was able to load
        {

            //Only Enable delete button if we could load subscription
            Button button = (Button) findViewById(R.id.details_delete_subscription);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if ( !editMode)  // If we are not in edit mode, let the user delete it
                    {

                        createDialog(subscriptionToDisplay.getID());
                    }

                }
            });

            //Only enable subscription button if we could load subscription
            editButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    editButton();

                }
            });
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

        // Change color of input.
        if (inputBool) {
            nameTarget.setTextColor(Color.parseColor(editableColor));
            paymentAmountTarget.setTextColor(Color.parseColor(editableColor));
            frequencyTarget.setTextColor(Color.parseColor(editableColor));
        } else {
            nameTarget.setTextColor(Color.parseColor(nonEditableColor));
            paymentAmountTarget.setTextColor(Color.parseColor(nonEditableColor));
            frequencyTarget.setTextColor(Color.parseColor(nonEditableColor));
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
        else if ( alreadyDeleted) // If the user tried to click the edit button after deleting the subscription
        {
            setGeneralError("Cannot edit subscription:\n Subscription has been deleted!", errorColor);

        }

        else // Else we are already in edit mode, and user is trying to save the changes.
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

            if ( valid ) // If valid, make error field invisible for now
            {
                generalErrorTarget.setVisibility(View.INVISIBLE);

            }


            // If everything was detected as valid, try to save the changed info
            if (valid) {
                try {

                    subscriptionToDisplay.setName(userNameInput);
                    subscriptionToDisplay.setPaymentFrequency(inputFrequency);
                    subscriptionToDisplay.setPayment(inputPaymentAmount);
                    subHandler.editWholeSubscription(subscriptionToDisplay.getID(), subscriptionToDisplay);

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


    //Create a Popup Message confirmation for our user, confirming if they really want to delete a subscription
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
                        setGeneralError(e.getMessage(), errorColor); // Some  unkown error occurred
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

    // Set what will show in the general Error message location
    private void setGeneralError(String inputMessage, String inputColor) {
        generalErrorTarget.setTextColor(Color.parseColor(inputColor));
        generalErrorTarget.setText(inputMessage);
        generalErrorTarget.setVisibility(View.VISIBLE);

    }


    //set what will show for the subscription information details in the various text fields
    private void setDetails(SubscriptionObj subscriptionToDisplay) {
        nameTarget.setText(subscriptionToDisplay.getName());
        paymentAmountTarget.setText(subscriptionToDisplay.getPaymentDollars() + "." +  String.format("%02d", subscriptionToDisplay.getPaymentCents())  );
        frequencyTarget.setText((subscriptionToDisplay.getPaymentFrequency()));
    }



}





