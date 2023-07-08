package com.track_it.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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


    private SubscriptionComparer subSorter = null;

    @Override // Make sure this function is overriding some default onCreate method
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Switch screen to display main page

        com.cook_ebook.persistence.utils.DBHelper.copyDatabaseToDevice(this); // Copy database
        subHandler = SetupParameters.getSubscriptionHandler();

        setUpButtonsAndInput();
        displayAllSubscriptions(); // Display all the subscriptions




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        assert(false);
         MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu,menu);
        return true;
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


        ImageButton filterButton;

        // Referencing and Initializing the button
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
                popupMenu.show();

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



    private void sortSubs(List<SubscriptionObj> listOfSubs)
    {
        SubscriptionSorter sorterHelper = new SubscriptionSorter(subSorter);
        sorterHelper.sortSubscriptions(listOfSubs);

    }

    // Display all the subscriptions currently in the database in a scrollable list
    private void displayAllSubscriptions()
    {

        searchString = searchInput.getQuery().toString().toLowerCase(); // Only show subs where the name contains the search string



        // Clear everything previously in list
        LinearLayout sv = (LinearLayout) this.findViewById(R.id.subscription_list);
        sv.removeAllViews();

        List<SubscriptionObj> listOfSubs = subHandler.getAllSubscriptions();

        sortSubs(listOfSubs); // Sort the subs

        boolean toggleColor = true; // Every second subscription will have a slightly different color

        for (SubscriptionObj curr : listOfSubs) {


            System.out.println("SEARCH STRING IS " + searchString);

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

