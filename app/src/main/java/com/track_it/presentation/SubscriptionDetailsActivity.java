package com.track_it.presentation;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.track_it.R;
import com.track_it.application.SetupParameters;
import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.logic.SubscriptionTagHandler;
import com.track_it.logic.exceptions.DatabaseException;
import com.track_it.logic.exceptions.SubscriptionException;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.exceptions.SubscriptionTagException;


//This is the activity java file for the edit description details page.
// Allow the user to view details, delete subscription, and edit subscription.

public class SubscriptionDetailsActivity extends AppCompatActivity {


    private int MAX_PAYMENT_DECIMALS = 2;
    private int MAX_DIGITS_BEFORE_DECIMAL; // Max payment amount in dollars (doesn't include cent count)
    private boolean alreadyDeleted = false; // Has the subscription been deleted during this session?
    private boolean editMode = false; // Are we in edit mode? Determines whether the input can be edited, and behaviour of the edit/save button

    // Various color - We should probably put this in some type of shared resource location later


    //Set by resource file
    private String errorColor; // error text color
    private String accomplishColor; // Accomplish text color
    private String saveButtonColor; // what color the save button will be
    private String editButtonColor; // what color the edit color button will be
    private String editableColor; // The color input will be when it is editable
    private String nonEditableColor;  // The color input will be when it is uneditable


    // Various targets for buttons, and text regions
    private EditText nameTarget;
    private EditText paymentAmountTarget;
    private TextView generalErrorTarget; // Where error messages are shown to user on this page

    private SubscriptionObj subscriptionToDisplay; // The subscription we will display, and allow the user to edit or delete
    private Button editButton;
    private Button backButton;
    private Button deleteButton;

    private EditText tagInput;

    //String constants, messages to display to user
    private static final String validEditMessage = "Subscription Edited!"; // if edit was successful
    private static final String successDeleteMessage = "Subscription Deleted!";  // if delete was successful
    private static final String alreadyDeletedMessage = "Subscription has already been Deleted!"; // what to show if user tries to delete twice

    private final String confirmationMsg = "Are you sure you want to delete the subscription?"; // confirmation message for deleting sub


    // Handler for the subscriptions
    private SubscriptionHandler subHandler;

    private boolean loadSub = false; // Were we able to load the subscription object using the info passed to this activity;


    private AutoCompleteTextView frequencyTarget;
    private TextInputLayout dropDownMenuParent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_details);


        setColors();

        // Set the various global targets and variables
        setTargets();

        constrainInput(); // Set up some filters, to constrain what is allowable input from user

        FrequencyMenu.initializeMenu(this, subHandler, frequencyTarget); //setup frequency menu

        enableInputChanges(editMode); // Disable editing sub details

        //Enable go back to home button
        enableGoBackButton();

        //Try to load the subscription from arguments passed to this activity
        if (getSubscription(subscriptionToDisplay)) // Make sure the subscription object was able to load
        {
            //Only Enable delete and Edit buttons if we could load subscription
            enableDeleteAndEditButtons();
        }

        TagSet.setTextWatcher(this,tagInput);


    }


    //Set colors by resource file
    private void setColors() {
        errorColor = getResources().getString(R.color.error_color);
        accomplishColor = getResources().getString(R.color.accomplish_color); // Accomplish text color
        saveButtonColor = getResources().getString(R.color.save_button_color); // what color the save button will be
        editButtonColor = getResources().getString(R.color.edit_button_color);// what color the edit color button will be
        editableColor = getResources().getString(R.color.editable_color); // The color input will be when it is editable
        nonEditableColor = getResources().getString(R.color.noneditable_color);  // The color input will be when it is uneditable
    }


    //Sets the variable targets so they can be used through out the activity
    private void setTargets() {


        // Get subscription handle
        subHandler = SetupParameters.getSubscriptionHandler();
        MAX_DIGITS_BEFORE_DECIMAL = SubscriptionInput.NumDigits(subHandler.getMaxPaymentDollars());

        //Frequency drop menu
        frequencyTarget = findViewById(R.id.AutoComplete_drop_menu);
        dropDownMenuParent = findViewById(R.id.parent_drop_menu);


        //Set button and message targets
        paymentAmountTarget = ((EditText) findViewById(R.id.detail_subscription_amount));
        nameTarget = ((EditText) findViewById(R.id.detail_subscription_name));
        generalErrorTarget = ((TextView) findViewById(R.id.details_general_error));
        backButton = (Button) findViewById(R.id.go_home);
        editButton = (Button) findViewById(R.id.details_edit_subscription);
        deleteButton = (Button) findViewById(R.id.details_delete_subscription);

        //Get tag input target
        tagInput = ((EditText) findViewById(R.id.tag_input));


    }


    //This function will set up a few filters, constraining what the user is allowed to enter for input.
    private void constrainInput() {

        //Set input some restriction for name input
        int maxLength = subHandler.getMaxNameLength();
        nameTarget.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)}); // Set max length of name

        // This physically constrains the user for what they can enter into the payment amount field ( How many digits before decimal, how many after)
        EditText etText = findViewById(R.id.detail_subscription_amount);  // Target Payment amount input
        etText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(MAX_PAYMENT_DECIMALS, MAX_DIGITS_BEFORE_DECIMAL)}); // Pass setFilters and array


    }


    private void enableGoBackButton() {
        //Enable go back to home button
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Go back to home page
                setContentView(R.layout.activity_main);
                finish();
            }
        });

    }


    private void enableDeleteAndEditButtons() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (editMode)  // If we are not in edit mode, let the user delete it
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


    //This will try to get the a subscription object from the logic layer, using the arguments passed to this activity via extras.
    // This function will return true if subscription could be retrieved, else returns false.
    private boolean getSubscription(SubscriptionObj subscription) {

        boolean loadedSub = false;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int subscriptionID = extras.getInt("subscriptionID"); //Get subscriptionID

            try {
                subscriptionToDisplay = subHandler.getSubscriptionByID(subscriptionID); // Get the subscription object associated with subscriptionID
                setDetails(subscriptionToDisplay);
                loadedSub = true;


            } catch (SubscriptionException e) // Failed to get subscription
            {
                setGeneralError(e.getMessage(), errorColor);
            } catch (DatabaseException e) {
                setGeneralError(e.getMessage(), errorColor);
            }
        } else  // Display an error letting the user know that there was an error getting subscription
        {
            setGeneralError("Invalid Selection for subscription", errorColor);
        }

        return loadedSub;

    }


    //Enable and disable frequency dropdown menu based on boolean argument passed to this function
    private void dropDownFrequencyMenuEnabled(boolean enable) {

        frequencyTarget.setEnabled(enable);
        dropDownMenuParent.setFocusableInTouchMode(enable);
        dropDownMenuParent.setEnabled(enable);
        frequencyTarget.setTextColor(getResources().getColor(R.color.black));

        //Change background color based on whether it is enabled or disabled
        if (enable) {
            frequencyTarget.setBackgroundColor(getResources().getColor(R.color.white_grey));
        } else {
            frequencyTarget.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }


    // This changes whether the input for subscription details can be edited by the user.
    // inputBool = true -> makes it editable
    // inputBool = false -> makes it not editable
    private void enableInputChanges(boolean inputBool) {
        nameTarget.setEnabled(inputBool);
        paymentAmountTarget.setEnabled(inputBool);
        tagInput.setEnabled(inputBool);
        tagInput.setTextColor(getResources().getColor(R.color.black));

        // Change color of input, and disable/enable drop down menu
        if (inputBool) {
            nameTarget.setTextColor(Color.parseColor(editableColor));
            paymentAmountTarget.setTextColor(Color.parseColor(editableColor));
            dropDownFrequencyMenuEnabled(true);
        } else {
            nameTarget.setTextColor(Color.parseColor(nonEditableColor));
            paymentAmountTarget.setTextColor(Color.parseColor(nonEditableColor));
            dropDownFrequencyMenuEnabled(false);
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

    // This runs when a user clicks the edit \ Save changes button
    private void editButton() {


        //If we are not in edit mode, and the subscription has not already been deleted  -then switch to edit mode
        if (editMode == false && !alreadyDeleted) {
            switchEditModes();


        } else if (alreadyDeleted) // Else if the subscription has already been deleted
        {
            setGeneralError("Cannot edit subscription:\n Subscription has been deleted!", errorColor); // Display error message, letting user know subscription has aready beendeleted

        } else // Else we are in edit mode, and user is trying to save the changes.
        {
            trySaveSubscriptionChanges(); //Will try to save user changes of the subscription details. Will display error if changes are invalid
        }
    }


    //Will try to save user changes of the subscription details. Will display error if changes are invalid
    private void trySaveSubscriptionChanges() {

        boolean valid = true;// Tells us if everything the user entered is valid

        // Get the frequency the user entered
        String inputFrequency = frequencyTarget.getText().toString().trim();
        try {

            subHandler.validateFrequency(inputFrequency);

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
            inputPaymentAmount = subInput.getPaymentAmountInput(paymentAmountTarget); // This function throws exceptions if payment invalid
            subHandler.validatePaymentAmount(inputPaymentAmount);
        } catch (SubscriptionException  e) // Payment amount not valid
        {
            // Display errors
            valid = false;
            setGeneralError(e.getMessage(), errorColor);
            generalErrorTarget.setVisibility(View.VISIBLE);
        }


        // Get the string the user entered for a name
        String userNameInput = nameTarget.getText().toString().trim(); // get string, and remove white spaces
        try {
            subHandler.validateName(userNameInput); // Check if name is valid

        } catch (SubscriptionException e) // Catch - name was not valid,
        {
            // Display errors
            valid = false;
            setGeneralError(e.getMessage(), errorColor);
            generalErrorTarget.setVisibility(View.VISIBLE);

        }

        // Get the string the user entered for tags
        String tagSting = tagInput.getText().toString().trim(); // get string, and remove white spaces
        try {
            subHandler.setTags(subscriptionToDisplay,tagSting); // Set tags
            subHandler.validateTagList(subscriptionToDisplay.getTagList()); //Validate tags

        } catch (SubscriptionException | SubscriptionTagException e) // Catch - tags not valid
        {
            // Display errors
            valid = false;
            setGeneralError(e.getMessage(), errorColor);
            generalErrorTarget.setVisibility(View.VISIBLE);

        }


        // If everything was detected as valid, try to save the changed info
        if (valid) {

            generalErrorTarget.setVisibility(View.INVISIBLE); //make error field invisible for now
            try {

                subscriptionToDisplay.setName(userNameInput);
                subscriptionToDisplay.setPaymentFrequency(inputFrequency);
                subscriptionToDisplay.setPayment(inputPaymentAmount);

                subHandler.editWholeSubscription(subscriptionToDisplay.getID(), subscriptionToDisplay);

                //Everything worked, so time to switch back to none-edit mode!
                switchEditModes();

            } catch (SubscriptionException | SubscriptionTagException  e)   //There was some error with the changes
            {
                // Display error
                setGeneralError(e.getMessage(), errorColor);
                generalErrorTarget.setVisibility(View.VISIBLE);
            } catch (DatabaseException e)  //Catch - Something wrong with database saving changes
            {
                // Display error
                setGeneralError(e.getMessage(), errorColor);
                generalErrorTarget.setVisibility(View.VISIBLE);

            }


        }

    }


    //What runs when a subscription is successfully deleted
    private void deleted() {
        //Display a toast message, and switch back to home screen
        Toast.makeText(this, successDeleteMessage, Toast.LENGTH_SHORT).show(); //Display "Subscription Deleted"
        setContentView(R.layout.activity_main); // Switch screen to display main page
        finish();
    }


    //switch edit modes based on boolean input
    private void switchEditModes() {
        editMode = !editMode;

        if (editMode)  // Switch to edit mode
        {
            enableInputChanges(true); // enable the inputs to be edited
            changeEditButton(); // toggle the edit button to change

        } else // Switch to non edit mode
        {
            enableInputChanges(editMode); // No longer allow inputs to be edited
            changeEditButton(); // Edit button changes back
            setGeneralError(validEditMessage, accomplishColor);
        }


    }


    //Create a Popup Message confirmation for our user, confirming if they really want to delete a subscription
    private void createDialog(int subscriptionToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SubscriptionDetailsActivity.this);
        builder.setTitle(confirmationMsg);

        // Make a Yes button, meaning that the user does in fact want to delete the subscription
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {

                    subHandler.removeSubscriptionByID(subscriptionToDelete); // Attempt to delete the sub
                    alreadyDeleted = true;
                    setGeneralError(successDeleteMessage, accomplishColor);
                    deleted();
                } catch (Exception e) {
                    if (!alreadyDeleted) {
                        setGeneralError(e.getMessage(), errorColor); // Some  unkown error occurred
                    } else {
                        setGeneralError(alreadyDeletedMessage, errorColor); // We already deleted it
                    }

                }
            }
        });
        // Make a No button - meaning the user does NOT want to delete subscription
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
        paymentAmountTarget.setText(subscriptionToDisplay.getPaymentDollars() + "." + String.format("%02d", subscriptionToDisplay.getPaymentCents()));
        frequencyTarget.setText((subscriptionToDisplay.getPaymentFrequency()), false);

        String allTags = "";
        for (  SubscriptionTag currTag : subscriptionToDisplay.getTagList())
        {
            allTags += currTag.getName() + " ";
        }

        TagSet.setTagColors(this, tagInput,allTags);
    }





}





