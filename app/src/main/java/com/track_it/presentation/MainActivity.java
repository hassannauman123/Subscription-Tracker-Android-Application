package com.track_it.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.track_it.R;
import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.presentation.util.SetupParameters;

import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

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


    @Override // Make sure this function is overriding some default onCreate method
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Switch screen to display main page

        com.cook_ebook.persistence.utils.DBHelper.copyDatabaseToDevice(this); // Copy database
        subHandler = SetupParameters.GetSubscriptionHandler();

        setUpButtonsAndInput();
        displayAllSubscriptions(); // Display all the subscriptions


    }

    private void setUpButtonsAndInput() {

        //Set target of buttons and input
        searchInput = (SearchView) this.findViewById(R.id.search_by_name);
        addSubButton = (Button) this.findViewById(R.id.button_subscription);


        //Set what happens when the user types in to the search bar
        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                displayAllSubscriptions(); //Display all subs (It will filter based on input from user)
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                 displayAllSubscriptions();  //Display all subs (It will filter based on input from user)
                return true;
            }
        });


        //Set what happens when user clicks add subscription button
        addSubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddSubscriptionActivity.class);
                startActivity(intent);
            }
        });

    }

    //Runs when this activity comes back
    protected void onRestart() {
        super.onRestart();


        View current = getCurrentFocus();
        if (current != null)  //Reset focus
        {
            current.clearFocus();
        }

        displayAllSubscriptions(); // Display all the subs


    }

    // Display all the subscriptions currently in the database in a scrollable list
    private void displayAllSubscriptions()
    {

        searchString = searchInput.getQuery().toString().toLowerCase(); // Only show subs where the name contains the search string

        // Clear everything previously in list
        LinearLayout sv = (LinearLayout) this.findViewById(R.id.subscription_list);
        sv.removeAllViews();

        List<SubscriptionObj> listOfSubs = subHandler.getAllSubscriptions();

        boolean toggleColor = true; // Every second subscription will have a slightly different color

        for (SubscriptionObj curr : listOfSubs) {


            if ( curr.getName().toLowerCase().contains(searchString))
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
                subscriptionBox.setOnClickListener(new View.OnClickListener() {
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

