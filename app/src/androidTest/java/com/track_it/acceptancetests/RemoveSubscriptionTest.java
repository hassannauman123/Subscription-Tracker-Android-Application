package com.track_it.acceptancetests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.os.SystemClock;

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

public class RemoveSubscriptionTest {



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



    //Create a Subscription, then remove it.
    @Test
    public void removeSub() {

        TestUtils.addSub(originalName, originalpayment, originalFrequency, ""); //ADD SUB

        SystemClock.sleep(TestUtils.getSleepTime());


        //Delete sub
        onView(withText("Name: " + originalName)).perform(click());
        onView(ViewMatchers.withId(R.id.details_delete_subscription)).perform(click()); // Click delete sub
        SystemClock.sleep(TestUtils.getSleepTime());// Let popup confirmation load
        onView(allOf(withId(android.R.id.button2), withText("Yes"))).perform(click());     //Click the yes button to confirm delete

        SystemClock.sleep(TestUtils.getSleepTime());// wait for main to load

        //Verify that the sub has been deleted
        onView(withText("Name: " + originalName)).check(doesNotExist());


        System.out.println("PASSED: remove test!");
        SystemClock.sleep(TestUtils.getSleepTime());

    }






}
