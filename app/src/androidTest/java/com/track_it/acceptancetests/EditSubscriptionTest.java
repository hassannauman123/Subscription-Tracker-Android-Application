package com.track_it.acceptancetests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
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

import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.track_it.R;
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

public class EditSubscriptionTest {




    private static final String originalName = "Lawn Care";
    private static final String originalpayment = "500";
    private static final String originalFrequency = "daily";
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


    @Test
    public void modifySub() {


        TestUtils.addSub(originalName, originalpayment, originalFrequency, ""); // Add the sub

        SystemClock.sleep(TestUtils.getSleepTime()); //Wait for main page to load
        onView(withText("Name: " + originalName)).perform(click()); // Click on sub in main


        SystemClock.sleep(TestUtils.getSleepTime()); //Wait for details page to load
        //New details that we will try to save
        String newName = "Tinker town";
        String newPaymentAmount = "394.46";
        String newFrequency = "bi-weekly";

        //EDIT SUB
        onView(ViewMatchers.withId(R.id.details_edit_subscription)).perform(click()); // Click edit sub
        SystemClock.sleep(TestUtils.getSleepTime());

        onView(withId(R.id.detail_subscription_name)).perform(replaceText(newName)); // change name
        onView(withId(R.id.detail_subscription_amount)).perform(replaceText(newPaymentAmount)); // Change amount
        SystemClock.sleep(TestUtils.getSleepTime()); // Let pop up menu load
        onView(withId(R.id.AutoComplete_drop_menu)).perform(click()); // Give new frequency
        onView(withText(newFrequency)).inRoot(RootMatchers.isPlatformPopup()).perform(click()); // change frequency

        SystemClock.sleep(TestUtils.getSleepTime());

        onView(withId(R.id.details_edit_subscription)).perform(click()); // Click edit sub
        onView(withId(R.id.go_home)).perform(click()); // go home
        SystemClock.sleep(TestUtils.getSleepTime()); //wait for main

        //Verify that the new sub details have saved
        onView(withText(containsString(newName))).check(matches(isDisplayed()));
        onView(withText(containsString(newPaymentAmount))).check(matches(isDisplayed()));
        onView(withText(containsString(newFrequency))).check(matches(isDisplayed()));

        System.out.println("PASSED: modify test!");
        SystemClock.sleep(TestUtils.getSleepTime());
    }





}
