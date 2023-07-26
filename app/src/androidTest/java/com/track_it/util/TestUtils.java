package com.track_it.util;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.track_it.R;
import com.track_it.application.SetupParameters;
import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.presentation.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.util.List;

public class TestUtils {
    private static File DB_SRC;

    private static boolean useRealDatabase = false; //Should we use real database? - Default false means use fakeDataBase


    private static final int SLEEP_TIME = 600;


    //The parameters we used to create the subs
    private static final int[] payments = {1956, 1000, 1350};
    private static final String[] stringPayments = {"10.00", "13.50", "19.56"};
    private static final String[] subNames = {"Youtube", "Amazon prime", "Dark-Zone Pass"};
    private static String[] subFrequency = new String[3];




    public static void refreshPage()
    {
        //Reload Activity
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent refresh = new Intent(context, MainActivity.class);
        refresh.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(refresh);

    }


    // Remove all subs in the database
    public static void clearDatabase(SubscriptionHandler subHandler)
    {

        List<SubscriptionObj> allSubs = subHandler.getAllSubscriptions();
        for (SubscriptionObj currSub : allSubs) {
            subHandler.removeSubscriptionByID(currSub.getID());
        }
    }

    public static int getSleepTime() {
        return SLEEP_TIME;
    }


    public static String[] getNames() {
        return subNames;
    }

    public static String[] getStringPayments() {
        return stringPayments;
    }

    public static String[] getFrequency() {
        return subFrequency;
    }

    public static String[] getFrequencyInOrder() {
        return subFrequency;
    }

    // Populate database with 3 subs
    public static void populateDatabaseWith3Subs(SubscriptionHandler subHandler) {


        subFrequency[0] = subHandler.getFrequencyNameList().get(0);
        subFrequency[1] = subHandler.getFrequencyNameList().get(3);
        subFrequency[2] = subHandler.getFrequencyNameList().get(4);

        //Create sub 1
        SubscriptionObj newSub1 = new SubscriptionObj(subNames[0], payments[0], subFrequency[2]);
        subHandler.addSubscription(newSub1);

        //Create sub 2
        String tags_2 = "movies";
        SubscriptionObj newSub2 = new SubscriptionObj(subNames[1], payments[1], subFrequency[0]);
        subHandler.setTags(newSub2, tags_2);
        subHandler.addSubscription(newSub2);

        // Create sub 3
        String tags_3 = "fun"; //Dark zone was fun
        SubscriptionObj newSub3 = new SubscriptionObj(subNames[2], payments[2], subFrequency[1]);
        subHandler.setTags(newSub3, tags_2);

        subHandler.addSubscription(newSub3);

    }


    // Commonly used function. This adds a subscription to the database, starting from the home page, and then goes back to home page.
    public static void addSub(String subName, String subPaymentAmount, String subFrequency, String subTags) {

        onView(withId(R.id.add_subscription_button)).perform(click()); // Click add sub - Starting from home page

        onView(withId(R.id.input_subscription_name)).perform(replaceText(subName)); // Give name
        onView(withId(R.id.input_payment_amount)).perform(replaceText(subPaymentAmount)); // Give payment
        onView(withId(R.id.subscription_list)).perform(click()); // Click frequency
        onView(withText(subFrequency)).inRoot(RootMatchers.isPlatformPopup()).perform(click());// Give frequency
        onView(withId(R.id.tag_input)).perform(replaceText(subTags)); // Give Tags

        onView(withId(R.id.submit_sub_button)).perform(click()); // Click add, and go back to home back

    }


}


