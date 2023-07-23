package com.track_it.acceptancetests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
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
    public void testSetup() {
        TestUtils.testSetup();
    }

    @After
    public void testTearDown() {
        TestUtils.testTearDown();
    }


    @Test
    public void modifySub() {


        TestUtils.addSub(originalName, originalpayment, originalFrequency, ""); // Add the sub

        onView(withText("Name: " + originalName)).perform(click()); // Click on sub in main

        //New details that we will try to save
        String newName = "Tinker town";
        String newPaymentAmount = "394.46";
        String newFrequency = "bi-weekly";


        //EDIT SUB
        onView(ViewMatchers.withId(R.id.details_edit_subscription)).perform(click()); // Click edit sub

        SystemClock.sleep(TestUtils.getSleepTime());// Sleep, just to more clearly see whats happening
        onView(withId(R.id.detail_subscription_name)).perform(replaceText(newName)); // change name
        onView(withId(R.id.detail_subscription_amount)).perform(replaceText(newPaymentAmount)); // Change amount
        onView(withId(R.id.AutoComplete_drop_menu)).perform(click()); // Give new frequency
        onView(withText(newFrequency)).inRoot(RootMatchers.isPlatformPopup()).perform(click()); // change frequency

        onView(withId(R.id.details_edit_subscription)).perform(click()); // Click edit sub
        onView(withId(R.id.go_home)).perform(click()); // go home
        SystemClock.sleep(TestUtils.getSleepTime());

        //Verify that the sub details have saved
        onView(withText("Name: " + newName)).check(matches(withText("Name: " + newName)));
        onView(withText("Payment Amount: $" + newPaymentAmount)).check(matches(withText("Payment Amount: $" + newPaymentAmount)));
        onView(withText("Frequency: " + newFrequency)).check(matches(withText("Frequency: " + newFrequency)));

        System.out.println("PASSED: modify test!");
        SystemClock.sleep(TestUtils.getSleepTime());
    }





}
