package com.track_it.Presentation;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.track_it.R;
import com.track_it.domainObject.SubscriptionObj;
import com.track_it.exception.DataBaseException;
import com.track_it.exception.SubscriptionException;
import com.track_it.logic.SubscriptionEditAndRemoveHandler;
import com.track_it.logic.SubscriptionGetHandler;
import com.track_it.logic.SubscriptionHandler;

public class SubscriptionDetailsActivity extends SubscriptionInput {


    private int MAX_PAYMENT_DECIMALS = 2;
    private int MAX_DIGITAL_BEFORE_DECIMAL =  SubscriptionHandler.getMaxPaymentDigitsBeforeDecimal();
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

        nameTarget =  ((EditText) findViewById(R.id.detail_subscription_name)) ;
        paymentAmountTarget =   ((EditText) findViewById(R.id.detail_subscription_amount));
        frequencyTarget = ((EditText) findViewById(R.id.detail_subscription_frequency));

        backButton = (Button) findViewById(R.id.go_home);
        generalErrorTarget = ((TextView) findViewById(R.id.details_general_error));
         editButton = (Button) findViewById(R.id.details_edit_subscription);

         SubscriptionGetHandler subGetHandler = new SubscriptionGetHandler();

        setFocus(false); // Disable editing sub details


        //Enable go back button
        backButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                finish();
            }
        });



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
                        createDialog(subscriptionToDisplay.getID());

                    }
                });



                //Enable edit button
                editButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                         editButton();

                    }
                });

            } catch (Exception e) {
                setGeneralError(e.getMessage(),errorColor);
            }
        } else {
            setGeneralError("Invalid Selection for subscription",errorColor);
        }

        // This physically constrains the user for what they can enter into the payment amount field ( How many digits before decimal, how many after)
        EditText etText = findViewById(R.id.detail_subscription_amount);  // Target Payment amount input
        etText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(MAX_PAYMENT_DECIMALS, MAX_DIGITAL_BEFORE_DECIMAL)}); // Pass setFilters and array

    }


    private void setFocus(boolean input)
    {
        nameTarget.setEnabled(input) ;
         paymentAmountTarget.setEnabled(input);
        frequencyTarget.setEnabled(input);


        String color2 = "#222222"; // Working fast
        if (input)
        {
            nameTarget.setTextColor(Color.parseColor(color2)) ;
            paymentAmountTarget.setTextColor(Color.parseColor(color2)) ;
            frequencyTarget.setTextColor(Color.parseColor(color2)) ;
        }
        else
        {
            nameTarget.setTextColor(Color.parseColor("#000000")) ;
            paymentAmountTarget.setTextColor(Color.parseColor("#000000")) ;
            frequencyTarget.setTextColor(Color.parseColor("#000000")) ;
        }

    }

    private void changeEditButton()
    {
        if (editMode == false)
        {
            editButton.setBackgroundColor(Color.parseColor(editButtonColor));
            editButton.setText("Edit Subscription");

        }
        else {
            editButton.setBackgroundColor(Color.parseColor(saveButtonColor));
            editButton.setText("Save Changes");
        }
    }

    // This runs when a user clicks the edit button
    private void editButton()
    {
        //If we are not in edit mode, and the subscription has not already been deleted
        if ( editMode == false && !alreadyDeleted)
        {

            editMode = true; // change to edit mode
            setFocus(true); // enable the inputs to be edited
            changeEditButton(); // toggle the edit button to change

        }
        else // Else we are already in edit mode, and user is try to save chages.
        {

            boolean valid = true;// Tells us if everything the user entered is valid
            SubscriptionHandler handler = new SubscriptionHandler();// users to get payment amount

            // Get the payment amount the user Enter
            SubscriptionInput subInput = new SubscriptionInput();
            int inputPaymentAmount  = subInput.getPaymentAmountInput( paymentAmountTarget,generalErrorTarget ); // This function throws exceptions, and sets user error message accordingly
            if ( inputPaymentAmount == Integer.MIN_VALUE) // Bit sloppy, but the return value will bit MIN_VALUE if any exception were thrown
            {
                valid = false;
            }


            // Get the string the user entered for a name
            String userNameInput = nameTarget.getText().toString().trim(); // get string, and remove white spaces
            try {
                handler.validateName(userNameInput); // Check if value
                generalErrorTarget.setVisibility(View.INVISIBLE);


            } catch (SubscriptionException e) // Else it threw an exception, and was not valid!
            {
                valid = false;
                setGeneralError(e.getMessage(), errorColor);
            }

            // Get the frequency the user entered
            String inputFrequency = frequencyTarget.getText().toString().trim();
            System.out.println("target is " + inputFrequency);
            try {

                handler.validateFrequency(inputFrequency);
                generalErrorTarget.setVisibility(View.INVISIBLE);

            } catch (SubscriptionException e) {
                valid = false;
                setGeneralError(e.getMessage(), errorColor);
            }


            // If everything was detected as valid, try to change the info
            if ( valid)
            {
                try {
                    SubscriptionEditAndRemoveHandler logicHandler = new SubscriptionEditAndRemoveHandler();

                    subscriptionToDisplay.setName(userNameInput);
                    subscriptionToDisplay.setPaymentFrequency(inputFrequency);
                    subscriptionToDisplay.setPayment(inputPaymentAmount);
                    logicHandler.editWholeSubscription (subscriptionToDisplay.getID(),subscriptionToDisplay);

                    //Everything worked, so time to switch back to none-edit mode!
                    editMode = false;
                    setFocus(false); // No longer allow inputs to be edited
                    changeEditButton(); // Edit button changes back
                    setGeneralError(validEditMessage, accomplishColor);
                }
                catch(Exception e)
                {
                    setGeneralError(e.getMessage(), errorColor);
                }

            }


        }
    }


    //PopupMessage Confirming to our user that they want to delelete a subscription
    private void createDialog(int subscriptionToDelete)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SubscriptionDetailsActivity.this);
        builder.setTitle(confirmationMsg);

        // Make a Yes button, meaning that the user does in fact want to delete the subscription
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick (DialogInterface dialog, int which)
            {
                SubscriptionEditAndRemoveHandler SubHandler = new SubscriptionEditAndRemoveHandler();
                try {

                    SubHandler.removeSubscriptionByID(subscriptionToDelete); // Attempt to delete the sub
                    alreadyDeleted= true;
                    setGeneralError(successDeleteMessage,accomplishColor);
                }
                catch (Exception e)
                {
                    if (!alreadyDeleted) {
                        setGeneralError(e.getMessage(), errorColor); // Some  unkown error
                    }
                    else {
                        setGeneralError(alreadyDeletedMessage, errorColor); // We already deleted it
                    }


                }
            }
        });
        // Make a No button
        builder.setPositiveButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick (DialogInterface dialog, int which)
            {
                // do need to do anything, as we won't delete subscription
            }
        });
        AlertDialog dialog = builder.create(); // create alert
        dialog.show(); // show it

    }

    private void setGeneralError(String inputMessage,String inputColor) {
        generalErrorTarget.setTextColor(Color.parseColor(inputColor));
        generalErrorTarget.setText(inputMessage);
        generalErrorTarget.setVisibility(View.VISIBLE);

    }

    private void setDetails(SubscriptionObj subscriptionToDisplay) {
        setName(subscriptionToDisplay.getName());
        setPaymentAmount( subscriptionToDisplay.getPaymentDollars() + "." + subscriptionToDisplay.getPaymentCents());
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





