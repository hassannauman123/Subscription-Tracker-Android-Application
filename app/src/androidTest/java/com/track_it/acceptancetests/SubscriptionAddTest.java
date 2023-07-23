package com.track_it.acceptancetests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
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

public class SubscriptionAddTest {



    private static final String originalName = "Lawn Care";
    private static final String originalpayment = "500";
    private static final String originalFrequency = "daily";
    private static final String originalTag1 = "customtag1";
    private static final String originalTag2 = "customtag2";


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



    @Test
    public void addSubTest() {

        SystemClock.sleep(TestUtils.getSleepTime());

        TestUtils.addSub(originalName, originalpayment, originalFrequency, "");

        // This verifies the subscription has been added.
        //Looks strange, but it will only work if the subscription shows up on main page with the exact correct text
        onView(withText("Name: " + originalName)).check(matches(withText("Name: " + originalName)));
        onView(withText("Payment Amount: $" + originalpayment + ".00")).check(matches(withText("Payment Amount: $" + originalpayment + ".00")));
        onView(withText("Frequency: " + originalFrequency)).check(matches(withText("Frequency: " + originalFrequency)));


        SystemClock.sleep(TestUtils.getSleepTime());

        System.out.println("PASSED: sub add Test!");


    }



}