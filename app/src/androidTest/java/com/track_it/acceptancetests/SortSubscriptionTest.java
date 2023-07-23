package com.track_it.acceptancetests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.PositionAssertions.isAbove;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.track_it.R;
import com.track_it.presentation.MainActivity;
import com.track_it.util.TestUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SortSubscriptionTest {


    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void testSetup() {
        TestUtils.testSetup();
    }

    @After
    public void testTearDown() {
        TestUtils.testTearDown();
    }


    //This tests sorting the subscriptions by name
    @Test
    public void sortByNameSubscriptionTest() {

        List<String> nameList = Arrays.asList(TestUtils.getNames());// Get the list of subscription names that currently exist
        Collections.sort(nameList); // sort that list alphabetically

        Context currContext = ApplicationProvider.getApplicationContext(); // Get current context so we can use string resources
        String sortCriteria = currContext.getResources().getString(R.string.sort_a_z); // The current sort button will have a text of this

        onView(withId(R.id.sort_button)).perform(click()); //Click the sort button
        onView(withText(containsString(sortCriteria))).perform(click()); // Sort alphabetically

        //Target and make sure all the subscriptions still show up
        ViewInteraction textViewAmazon = onView(withText(containsString("Name: " + nameList.get(0))));
        textViewAmazon.check(matches(withText("Name: " + nameList.get(0))));

        ViewInteraction textViewDark = onView(withText(containsString("Name: " + nameList.get(1))));
        textViewDark.check(matches(withText("Name: " + nameList.get(1))));

        ViewInteraction textViewYoutube = onView(withText(containsString("Name: " + nameList.get(2))));
        textViewYoutube.check(matches(withText("Name: " + nameList.get(2))));


        //Make sure the subscription show up in the correct order
        textViewAmazon.check(isCompletelyAbove(withText("Name: " + nameList.get(1))));
        textViewDark.check(isCompletelyAbove(withText("Name: " + nameList.get(2))));


    }

    //This tests sorting the subscriptions by payment
    @Test
    public void sortByPaymentTest() {

        String[] paymentInOrder = TestUtils.getStringPayments(); // Get all the existing payment amount for subscriptions in order

        Context currContext = ApplicationProvider.getApplicationContext(); // Get current context so we can use string resources
        String sortCriteria = currContext.getResources().getString(R.string.sort_payment); // The current sort button will have a text of this

        onView(withId(R.id.sort_button)).perform(click());
        onView(withText(containsString(sortCriteria))).perform(click());


        //Target, and make sure all the subscriptions still show up
        ViewInteraction textView10 = onView(withText(containsString("$" + paymentInOrder[0])));
        textView10.check(matches(withText("Payment Amount: $" + paymentInOrder[0])));

        ViewInteraction textView1350 = onView(withText(containsString(paymentInOrder[1])));
        textView1350.check(matches(withText("Payment Amount: $" + paymentInOrder[1])));

        ViewInteraction textView1956 = onView(withText(containsString(paymentInOrder[2])));
        textView1956.check(matches(withText("Payment Amount: $" + paymentInOrder[2])));


        //Make sure the subscriptions show up in the correct order
        textView10.check(isCompletelyAbove(withText("Payment Amount: $" + paymentInOrder[1])));
        textView1350.check(isCompletelyAbove(withText("Payment Amount: $" + paymentInOrder[2])));

    }

    //This tests sorting the subscriptions by frequency
    @Test
    public void sortByFrequencyTest() {

        String[] frequencyInOrder = TestUtils.getFrequencyInOrder();

        Context currContext = ApplicationProvider.getApplicationContext(); // Get current context so we can use string resources
        String sortCriteria = currContext.getResources().getString(R.string.sort_frequency); // The current sort button will have a text of this

        onView(withId(R.id.sort_button)).perform(click());
        onView(withText(containsString(sortCriteria))).perform(click());


        //Target and make sure all the subscriptions show up
        ViewInteraction textViewDaily = onView(withText(containsString("Frequency: " + frequencyInOrder[0])));
        textViewDaily.check(matches(withText("Frequency: " + frequencyInOrder[0])));

        ViewInteraction textViewMonthly = onView(withText(containsString("Frequency: " + frequencyInOrder[1])));
        textViewMonthly.check(matches(withText("Frequency: " + frequencyInOrder[1])));

        ViewInteraction textViewYearly = onView(withText(containsString("Frequency: " + frequencyInOrder[2])));
        textViewYearly.check(matches(withText("Frequency: " + frequencyInOrder[2])));


        //Make sure the subscriptions show up in the correct order
        textViewDaily.check(isCompletelyAbove(withText("Frequency: " + frequencyInOrder[1])));
        textViewMonthly.check(isCompletelyAbove(withText("Frequency: " + frequencyInOrder[2])));


    }

}
