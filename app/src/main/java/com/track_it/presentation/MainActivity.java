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

    @Override // Make sure this function is overriding some default onCreate method
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Switch screen to display main page

        com.cook_ebook.persistence.utils.DBHelper.copyDatabaseToDevice(this);
        subHandler  = SetupParameters.GetSubscriptionHandler();

        displayAllSubscriptions(); // Display all the subscriptions

         // Target add subscription button, and set what happens to it when clicked
        Button button = (Button) this.findViewById(R.id.button_subscription);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, AddSubscriptionActivity.class);
                startActivity(intent);

            }
        });


    }

    //Runs when this activity comes back
    protected void onRestart() {
        super.onRestart();
        displayAllSubscriptions();

    }

    // Display all the subscriptions currently in the database in a scrollable list
    private void displayAllSubscriptions() {

        // Clear everything previously in list
        LinearLayout sv = (LinearLayout) this.findViewById(R.id.subscription_list);
        sv.removeAllViews();

        List<SubscriptionObj> listOfSubs = subHandler.getAllSubscriptions();

        boolean toggleColor = true;
        int color1 = Color.parseColor("#EEEEEE");
        int color2 = Color.parseColor("#FFFFFF");

        for ( int i =0 ; i <listOfSubs.size(); i++ )
        {

            // Current subscription object we are working on
            SubscriptionObj curr = listOfSubs.get(i);

            // Create a new box to display subscription
            View subscriptionBox = getLayoutInflater().inflate(R.layout.subscription_box, sv, false);


            // Set Name of subscription

            TextView targetName =  subscriptionBox.findViewById(R.id.subscription_name);
            targetName.setText("Name: " + curr.getName());


            // Set Frequency
            TextView targetFrequency =  subscriptionBox.findViewById(R.id.subscription_frequency);
            targetFrequency.setText("Frequency: " + curr.getPaymentFrequency());



            // Set Payment amount
            TextView targetPaymentAmount =  subscriptionBox.findViewById(R.id.subscription_amount);
            targetPaymentAmount.setText("Payment Amount: $" + curr.getPaymentDollars() + "." +  String.format("%02d", curr.getPaymentCents()));



            //Set ID - Just as a reminder, these might not be unique ID's on this page, as other elements may have these ID numbers
            // but for how we are using them right now it's fine.
            subscriptionBox.setId(curr.getID());
            toggleColor = !toggleColor;
            if ( toggleColor) {
                subscriptionBox.setBackgroundColor(color1);
            }
            else {
                subscriptionBox.setBackgroundColor(color2);
            }


            //Set what happens when the user does a long click on a subscription box
            subscriptionBox.setOnLongClickListener(
                new View.OnLongClickListener()
                {
                    public boolean onLongClick(View v)
                    {
                        Intent subDetailsIntent = new Intent(MainActivity.this, SubscriptionDetailsActivity.class);
                        subDetailsIntent.putExtra("subscriptionID", v.getId() );
                        startActivity(subDetailsIntent);
                        return true;
                    }
                }
            );

            //Set what happens when the user does clicks on a subscription box
            subscriptionBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent subDetailsIntent = new Intent(MainActivity.this, SubscriptionDetailsActivity.class);
                    subDetailsIntent.putExtra("subscriptionID", v.getId() );
                    startActivity(subDetailsIntent);
                }
            });


            sv.addView(subscriptionBox);
        }
    }




}

