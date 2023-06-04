package com.track_it.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.track_it.R;
import com.track_it.domainObject.SubscriptionObj;
import com.track_it.persistence.DataBase;

import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override // Make sure this function is overriding some default onCreate method
    protected void onCreate(Bundle savedInstanceState)  // when this crated
    {
        DataBase.fillFakeData();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Switch screen to display main pag
        displayAllSubscriptions();



        //Button sections

        // Target subscription button, and switch to that intent when clicked
        Button button = (Button) this.findViewById(R.id.button_subscription);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddSubscriptionActivity.class);

                startActivity(intent);


            }
        });


    }

    protected void onRestart() {
        super.onRestart();
        displayAllSubscriptions();

    }

    private void displayAllSubscriptions() {

        DataBase DBHelper = new DataBase();

        // Clear everything previously in list
        LinearLayout sv = (LinearLayout) this.findViewById(R.id.subscription_list);
        sv.removeAllViews();

        ArrayList<SubscriptionObj> listOfSubs = DBHelper.getAllSubscriptions();

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


            //Set ID - Just as a reminder, these might not be unique ID's on this page, as other elements by have these ID numbers
            // but for how we are using them right now it's fine
            subscriptionBox.setId(curr.getID());


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

