package com.track_it.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.track_it.R;
import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.comparators.*;
import com.track_it.logic.exceptions.DataBaseException;
import com.track_it.logic.exceptions.SubscriptionException;
import com.track_it.logic.exceptions.SubscriptionInvalidFrequencyException;
import com.track_it.presentation.util.SetupParameters;


import android.content.Intent;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


//
//  This is the presentation class for the main home page of the app.
//  It will be instantiated when the android app first starts
//
//  Currently it displays all subscriptions in a scrollable list, and has a add subscription button.
//  Clicking on any subscription should open a new page that allows you to edit or delete the sub.
//  It also has sort and search input, to filter and sort the subscriptions.
//


public class MainActivity extends AppCompatActivity {


    private SubscriptionHandler subHandler;

    private Button addSubButton; // button to add a subscription
    private SearchView searchInput; // Search input target
    private ImageButton filterButton; // target of a drop down button for sorting subscriptions

    private String searchString = ""; // Only subscriptions that contain the searchString will be shown ( by default all subs are shown)

    private List<SubscriptionObj> listOfSubs; // hold all the subscriptions to display
    private  Comparator <SubscriptionObj> subSorter = null; //How we will sort subscriptions (By default we won't sort the subscriptions)
    private TextView errorDisplay; // target to display errors
    private  LinearLayout displaySubList; // Target for subscription list

    private boolean firstColor = true; // Every second subscription will have a slightly different color, and this will toggle between the colors




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Switch screen to display main page

        com.cook_ebook.persistence.utils.DBHelper.copyDatabaseToDevice(this); // Copy database


        //Get subscription handler
        subHandler = SetupParameters.getSubscriptionHandler();

        setTargets(); //Set the targets for the global variables
        getSubList(); // Get list of subs from database, and store in listOfSubs
        setUpButtonsAndInput(); // Setup the input fields and buttons
        displayAllSubscriptions(); // Display all the subscriptions


    }


    //Runs when this activity comes back
    protected void onRestart() {
        super.onRestart();
        disableError(); // Clear any errors
        getSubList(); //Get a new list of subs ( in case any subs were added, deleted, or modified)

        View current = getCurrentFocus();
        if (current != null)  //Reset focus
        {
            current.clearFocus();
        }

        displayAllSubscriptions(); // Display all the subs
    }



    //Set all the targets for the global variables for this activity
    private void setTargets()
    {
        //Where errors will show
        errorDisplay = (TextView) this.findViewById(R.id.details_general_error);
        displaySubList = (LinearLayout) this.findViewById(R.id.subscription_list);

        //Set target of add sub button
        addSubButton = (Button) this.findViewById(R.id.add_subscription_button);

        //Search bar target
        searchInput = (SearchView) this.findViewById(R.id.search_by_name);

        // target filter button
        filterButton = (ImageButton) findViewById(R.id.sort_button);


    }


    //Get the all subscriptions from the logic layer, and store in listOfSubs
    private void getSubList()
    {
        try {
            //Get subscription handler, and list of subscriptions
            subHandler = SetupParameters.getSubscriptionHandler();
            listOfSubs = subHandler.getAllSubscriptions();
         }
        catch( SubscriptionException e) //Something went wrong with getting subs, display error
        {
            listOfSubs = new ArrayList<SubscriptionObj>(); // make list empty
            enableError(e.getMessage());

        }
        catch( DataBaseException e)  //Something went wrong with getting subs, display error
        {
            listOfSubs = new ArrayList<SubscriptionObj>(); // make list empty
            enableError(e.getMessage());
        }
    }

    //Set the error message, and make it visible
    private void enableError(String inputMsg)
    {
        errorDisplay.setText(inputMsg);
        errorDisplay.setVisibility(View.VISIBLE);

    }

    //Remove the error message, and make it invisible
    private void disableError()
    {
        errorDisplay.setText("");
        errorDisplay.setVisibility(View.INVISIBLE);
    }

    private void setUpButtonsAndInput() {
        enableAddSubButton();
        enableSearchInput();
        enableSortFilterInput();
    }

    //Enable the add subscription button
    private void enableAddSubButton() {


        //Set what happens when user clicks add subscription button
        addSubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddSubscriptionActivity.class);
                startActivity(intent);
            }
        });
    }

    //Enable search input
    private void enableSearchInput() {



        //Set what happens when the user types into the search bar
        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query)  // What happens when user hits "enter" in search bar
            {

                displayAllSubscriptions(); //Display all subs (It will filter based on input from user)

                //Close the keyboard
                View current = getCurrentFocus();
                if (current != null)  //Reset focus
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(current.getWindowToken(), 0);

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) //What happens when user types in search bar
            {

                displayAllSubscriptions();  //Display all subs (It will filter based on input from user)
                return true;
            }
        });
    }


    //Enable the sort input - Allows user to sort subscriptions
    private void enableSortFilterInput() {



        // Setting onClick behavior to the button
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, filterButton);

                // Inflating popup menu from sort_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());

                //Set what happens when user selects option from pop up sort menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //Setting what filter to use to sort the subscriptions (ie, set subSorter)
                        String sortInput = menuItem.getTitle().toString();
                        if (sortInput.equals(getString(R.string.sort_a_z))) {
                            subSorter = new CompareSubscriptionName();
                        } else if (sortInput.equals(getString(R.string.sort_payment))) {
                            subSorter = new CompareSubscriptionPayment();
                        } else if (sortInput.equals(getString(R.string.sort_frequency))) {
                            subSorter = new CompareSubscriptionFrequency();
                        } else {
                            subSorter = null;
                        }
                        displayAllSubscriptions();
                        return true;
                    }


                });

                popupMenu.show(); //Enable the popup menu, allowing user to choose sort criteria

            }
        });
    }


    //Sort the subscription using Comparator object (sortSubsBy).
    // If sortSubsBy is null, subs will not be sorted
    private void sortSubs(List<SubscriptionObj> listOfSubs,   Comparator <SubscriptionObj> sortSubsBy)
    {

        try {
            if (sortSubsBy != null) {
                Collections.sort(listOfSubs, sortSubsBy);
            }
        }
        catch(SubscriptionInvalidFrequencyException e)
        {
            enableError("can't sort subs because " + e.getMessage());
        }

    }

    // Display all the subscriptions currently in listOfSubs in a scrollable list
    private void displayAllSubscriptions() {

        searchString = searchInput.getQuery().toString().toLowerCase(); // Get the search string ( We will only show subs where the name contains the search string)
        sortSubs(listOfSubs, subSorter); // Sort the subscription

        // Clears all the subscription boxes that were previously displayed on screen
        displaySubList.removeAllViews();


         firstColor = true; // Every second subscription will have a slightly different color

        //Create a subscription box for each subscription that we will show
        for (SubscriptionObj curr : listOfSubs) {

            if (curr.getName().toLowerCase().contains(searchString)) // only show this subscription if it matches the search criteria
            {
                createBoxForSubscription(curr);
            }

        }
    }


    //Create a box for subInput, and add it to displaySubList, and then display it
    private void createBoxForSubscription(SubscriptionObj subInput)
    {

        // Create a new box to display subscription
        View subscriptionBox = getLayoutInflater().inflate(R.layout.subscription_box, displaySubList, false);


        // Set Name of subscription to display
        TextView targetName = subscriptionBox.findViewById(R.id.subscription_name);
        targetName.setText("Name: " + subInput.getName());


        // Set Frequency to display
        TextView targetFrequency = subscriptionBox.findViewById(R.id.subscription_frequency);
        targetFrequency.setText("Frequency: " + subInput.getPaymentFrequency());


        // Set Payment amount to display
        TextView targetPaymentAmount = subscriptionBox.findViewById(R.id.subscription_amount);
        targetPaymentAmount.setText("Payment Amount: $" + subInput.getPaymentDollars() + "." + String.format("%02d", subInput.getPaymentCents()));


        //Set ID - Just as a reminder, these might not be unique ID's on this page, as other elements may have these ID numbers
        // but for how we are using them right now it's fine.
        subscriptionBox.setId(subInput.getID());


        //Every other subscription has a different background color
        firstColor = !firstColor;
        if (firstColor) {
            subscriptionBox.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            subscriptionBox.setBackgroundColor(getResources().getColor(R.color.white));
        }


        setSubscriptionBoxBehaviour(subscriptionBox); //Set behavior of box whens it's clicked
        displaySubList.addView(subscriptionBox); // display box
    }



    //Set the behavior of setSubscriptionBoxBehaviour when it's clicked
    private void setSubscriptionBoxBehaviour(View subscriptionBox)
    {

        //Set what happens when the user does a long click on a subscription box
        subscriptionBox.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Intent subDetailsIntent = new Intent(MainActivity.this, SubscriptionDetailsActivity.class);
                subDetailsIntent.putExtra("subscriptionID", v.getId());
                startActivity(subDetailsIntent);
                return true;
            }
        });

        //Set what happens when the user does a short click on a subscription box
        subscriptionBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent subDetailsIntent = new Intent(MainActivity.this, SubscriptionDetailsActivity.class);
                subDetailsIntent.putExtra("subscriptionID", v.getId());
                startActivity(subDetailsIntent);
            }
        });

    }


}

