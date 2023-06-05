package com.track_it.presentation;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.track_it.R;
import com.track_it.domainObject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.persistence.DataBase;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


//
//  This is the presentation class for the main home page of the app.
//  It will be instantiated when the android app first starts
//
//  Currently it displays all subscriptions in scrollable list, and has a button to add a new sub.
//   Clicking on any sub should open a new page that allows you to edit or delete the sub.
//

public class MainActivity extends AppCompatActivity {

    @Override // Make sure this function is overriding some default onCreate method
    protected void onCreate(Bundle savedInstanceState)
    {
        DataBase.fillFakeData(); // Fill the database with fake data?

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Switch screen to display main page

        displayAllSubscriptions(); // Display all the subscriptions

        //Button section

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

    //Runs when this app comes back
    protected void onRestart() {
        super.onRestart();
        displayAllSubscriptions();

    }

    // Display all the subscriptions currently in the database in a scrollable list
    private void displayAllSubscriptions() {

        SubscriptionHandler subHandler = new SubscriptionHandler();

        // Clear everything previously in list
        LinearLayout sv = (LinearLayout) this.findViewById(R.id.subscription_list);
        sv.removeAllViews();

        ArrayList<SubscriptionObj> listOfSubs = subHandler.getAllSubscriptions();

        for ( int i =0 ; i <listOfSubs.size(); i++ )
        {


            // Current subscription object we are working on
            SubscriptionObj curr = listOfSubs.get(i);

            // Create a new box to display subscription
            View subscriptionBox = getLayoutInflater().inflate(R.layout.subscription_box, sv, false);


            // Set Name of subscription
            TextView targetName =  subscriptionBox.findViewById(R.id.subscription_name);
            targetName.setText("Name is : " + curr.getName() );


            // Set Frequency
            TextView targetFrequency =  subscriptionBox.findViewById(R.id.subscription_frequency);
            targetFrequency.setText("Frequency: " + curr.getPaymentFrequency());



            // Set Payment amount
            TextView targetPaymentAmount =  subscriptionBox.findViewById(R.id.subscription_amount);
            targetPaymentAmount.setText("Payment Amount: $" + curr.getPaymentDollars() + "." +  curr.getPaymentCents() );


            //Set ID - Just as a reminder, these might not be unique ID's on this page, as other elements may have these ID numbers
            // but for how we are using them right now it's fine.
            subscriptionBox.setId(curr.getID());


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

