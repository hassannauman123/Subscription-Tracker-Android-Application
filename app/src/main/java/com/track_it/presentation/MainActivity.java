package com.track_it.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.track_it.R;
import com.track_it.domainobject.SubscriptionObj;
 import com.track_it.logic.SubscriptionCompare.SubscriptionComparer;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.SubscriptionCompare.*;
import com.track_it.logic.SubscriptionSorter;
import com.track_it.presentation.util.SetupParameters;

import android.content.Intent;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;


//
//  This is the presentation class for the main home page of the app.
//  It will be instantiated when the android app first starts
//
//  Currently it displays all subscriptions in a scrollable list, and got to a page to add a new subscription.
//   Clicking on any subscription should open a new page that allows you to edit or delete the sub.
//


public class MainActivity extends AppCompatActivity {


    private SubscriptionHandler subHandler;

    private Button addSubButton;
    private SearchView searchInput;
    private String searchString = ""; // Only display subscription that contain the searchString ( by default all subs are shown)


    private  List<SubscriptionObj> listOfSubs; // hold all the subscriptions to display
    private SubscriptionComparer subSorter = null;

    @Override // Make sure this function is overriding some default onCreate method
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Switch screen to display main page

        com.cook_ebook.persistence.utils.DBHelper.copyDatabaseToDevice(this); // Copy database

        //Get subscription handler, and list of subscriptions
        subHandler = SetupParameters.getSubscriptionHandler();
        listOfSubs = subHandler.getAllSubscriptions();


        setUpButtonsAndInput(); // Setup the input and buttons
        displayAllSubscriptions(); // Display all the subscriptions


    }



    //Runs when this activity comes back
    protected void onRestart() {
        super.onRestart();
        listOfSubs = subHandler.getAllSubscriptions(); //Get a new list of subs ( in case any where added, deleted, or modified)

        View current = getCurrentFocus();
        if (current != null)  //Reset focus
        {
            current.clearFocus();
        }

        displayAllSubscriptions(); // Display all the subs

    }



    private void setUpButtonsAndInput()
    {
        enableAddSubButton();
        enableSearchInput();
        enableSortFilterInput();
    }

    //Enable the add subscription button
    private void enableAddSubButton()
    {
        //Set target of buttons and input
        addSubButton = (Button) this.findViewById(R.id.add_subscription_button);


        //Set what happens when user clicks add subscription button
        addSubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddSubscriptionActivity.class);
                startActivity(intent);
            }
        });
    }

    //Enable search input
    private void enableSearchInput()
    {

        searchInput = (SearchView) this.findViewById(R.id.search_by_name);

        //Set what happens when the user types in to the search bar
        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query)  // What happens when user hits "enter" in search bar
            {

                displayAllSubscriptions(); //Display all subs (It will filter based on input from user)


                //Close the keyboard
                View current = getCurrentFocus();
                if (current != null)  //Reset focus
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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


    //Enable the filter input - Allows user to sort subscriptions
    private void enableSortFilterInput()
    {

        ImageButton filterButton; // Filter drop down button

        // Referencing and Initializing the filter button
        filterButton = (ImageButton) findViewById(R.id.clickBtn);

        // Setting onClick behavior to the button
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, filterButton);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        //Setting what filter to use to sort the subscriptions
                        String sortInput =  menuItem.getTitle().toString();
                        if ( sortInput.equals(getString(R.string.sort_a_z)) )
                        {
                            subSorter = new CompareSubscriptionName();
                        }
                        else if ( sortInput.equals(getString(R.string.sort_payment)) )
                        {
                            subSorter = new CompareSubscriptionPayment();
                        }
                        else if ( sortInput.equals(getString(R.string.sort_frequency)))
                        {
                            subSorter =  new CompareSubscriptionFrequency();
                        }
                        else {
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



    //Sort the subscription using SubscriptionComparer.
    // If SubscriptionComparer is null, subs will not be sorted
    private void sortSubs(List<SubscriptionObj> listOfSubs, SubscriptionComparer sortSubsBy )
    {
        SubscriptionSorter sorterHelper = new SubscriptionSorter(sortSubsBy);
        sorterHelper.sortSubscriptions(listOfSubs);

    }

    // Display all the subscriptions currently in the database in a scrollable list
    private void displayAllSubscriptions()
    {

        searchString = searchInput.getQuery().toString().toLowerCase(); // Get the search string ( We will only show subs where the name contains the search string)
        sortSubs(listOfSubs, subSorter); // Sort the subscriptions


        // Clear everything previously in list
        LinearLayout sv = (LinearLayout) this.findViewById(R.id.subscription_list);
        sv.removeAllViews();


        boolean toggleColor = true; // Every second subscription will have a slightly different color

        //Create a subscription box for each subscription that we will show
        for (SubscriptionObj curr : listOfSubs) {



            if ( curr.getName().toLowerCase().contains(searchString)) // only show this subscription if it matches the search criteria
            {

                // Create a new box to display subscription
                View subscriptionBox = getLayoutInflater().inflate(R.layout.subscription_box, sv, false);


                // Set Name of subscription
                TextView targetName = subscriptionBox.findViewById(R.id.subscription_name);
                targetName.setText("Name: " + curr.getName());


                // Set Frequency
                TextView targetFrequency = subscriptionBox.findViewById(R.id.subscription_frequency);
                targetFrequency.setText("Frequency: " + curr.getPaymentFrequency());


                // Set Payment amount
                TextView targetPaymentAmount = subscriptionBox.findViewById(R.id.subscription_amount);
                targetPaymentAmount.setText("Payment Amount: $" + curr.getPaymentDollars() + "." + String.format("%02d", curr.getPaymentCents()));


                //Set ID - Just as a reminder, these might not be unique ID's on this page, as other elements may have these ID numbers
                // but for how we are using them right now it's fine.
                subscriptionBox.setId(curr.getID());


                //Every other subscription has a different background color
                toggleColor = !toggleColor;
                if (toggleColor) {
                    subscriptionBox.setBackgroundColor(getResources().getColor(R.color.grey));
                } else {
                    subscriptionBox.setBackgroundColor(getResources().getColor(R.color.white));
                }


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
                subscriptionBox.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v) {
                        Intent subDetailsIntent = new Intent(MainActivity.this, SubscriptionDetailsActivity.class);
                        subDetailsIntent.putExtra("subscriptionID", v.getId());
                        startActivity(subDetailsIntent);
                    }
                });


                sv.addView(subscriptionBox);
            }
        }
    }


}

