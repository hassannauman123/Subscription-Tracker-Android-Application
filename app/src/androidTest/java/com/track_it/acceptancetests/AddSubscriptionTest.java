package com.track_it.acceptancetests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.track_it.application.SetupParameters;
import com.track_it.presentation.MainActivity;
import com.track_it.util.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@LargeTest
@RunWith(AndroidJUnit4.class)

public class AddSubscriptionTest
{


    //Details for a subscription to be added
    private static final String originalName = "Lawn Care";
    private static final String originalpayment = "500";
    private static final String originalFrequency = "weekly";
    private static final String originalTag1 = "customtag1";
    private static final String originalTag2 = "customtag2";


    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void testSetup()
    {
        TestUtils.clearDatabase(SetupParameters.getSubscriptionHandler());
        TestUtils.refreshPage();

    }

    @After
    public void testTearDown() {
        TestUtils.clearDatabase(SetupParameters.getSubscriptionHandler());
    }



    //Test adding as subscription.
    // This will add a subscription, and then verify that it shows up with correct information
    @Test
    public void addSubTest() {

        SystemClock.sleep(TestUtils.getSleepTime());

        TestUtils.addSub(originalName, originalpayment, originalFrequency, ""); // This adds a subscription using UI clicks

        // This verifies the subscription has been added, and shows up correctly as expected
        onView(withText(containsString(originalName))).check(matches(isDisplayed()));
        onView(withText(containsString("$" + originalpayment + ".00"))).check(matches(isDisplayed()));
        onView(withText(containsString(originalFrequency))).check(matches(isDisplayed()));


        SystemClock.sleep(TestUtils.getSleepTime());

        System.out.println("PASSED: sub add Test!");


    }



}