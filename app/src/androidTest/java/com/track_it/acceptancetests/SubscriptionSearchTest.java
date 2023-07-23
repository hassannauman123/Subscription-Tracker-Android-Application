package com.track_it.acceptancetests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.track_it.presentation.MainActivity;
import com.track_it.util.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@LargeTest
@RunWith(AndroidJUnit4.class)

public class SubscriptionSearchTest {



    //Details for 1 subscription
    private final String partialName = "cart";
    private static final String originalName = "Streaming Cartoon Central";
    private static final String originalPayment = "500";
    private static final String originalFrequency = "daily";
    private static final String originalTag1 = "customtag1";
    private static final String originalTag2 = "customtag2";

    //Details for another subscription
    private static final String originalName2 = "Costco membership";
    private static final String originalPayment2 = "1000";
    private static final String originalFrequency2 = "monthly";
    private static final String originalTag3 = "customtag1";
    private static final String originalTag4 = "customtag2";



    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void testSetup() {
        TestUtils.testSetup();
    }

    @After
    public void testTearDown() {
        TestUtils.testTearDown();
    }




    //Test the search by name for subscriptions feature.
    @Test
    public void searchTest() {

        //Add both subscriptions
        TestUtils.addSub(originalName, originalPayment, originalFrequency, originalTag1 + " " + originalTag2); //Add sub 1
        TestUtils.addSub(originalName2, originalPayment2, originalFrequency2, originalTag1 + " " + originalTag2); //Add sub 2

        SystemClock.sleep(TestUtils.getSleepTime());


        TestUtils.typeInSearch(partialName); // Search for part of the name


        // Verify that the first sub shows up with the search applied
        onView(withText("Name: " + originalName)).check(matches(withText("Name: " + originalName)));

        //Verify that the other sub does no show up
        onView(withText("Name: " + originalName2)).check(doesNotExist());


        System.out.println("PASSED: search test!");
        SystemClock.sleep(TestUtils.getSleepTime());

    }


}
