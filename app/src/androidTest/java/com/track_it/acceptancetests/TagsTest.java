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

public class TagsTest {



    private static final String originalName = "Park pass";
    private static final String originalpayment = "1600";
    private static final String originalFrequency = "monthly";
    private static final String originalTag1 = "tag1";
    private static final String originalTag2 = "tag2";


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
    public void addTagTest() {

        TestUtils.addSub(originalName, originalpayment, originalFrequency, originalTag1 + " " + originalTag2); //ADD SUB

        SystemClock.sleep(TestUtils.getSleepTime());

        //Go to sub details
        onView(withText(containsString(originalName))).perform(click());
        SystemClock.sleep(TestUtils.getSleepTime());

        //Make sure tags show up
        onView(withText(containsString(originalTag1))).check(matches(isDisplayed()));
        onView(withText(containsString(originalTag2))).check(matches(isDisplayed()));


        System.out.println("PASSED: add tags test!");
        SystemClock.sleep(TestUtils.getSleepTime());

    }

    @Test
    public void filterTagTest() {

        TestUtils.addSub(originalName, originalpayment, originalFrequency, originalTag1 + " " + originalTag2); //Add sub with tags

        SystemClock.sleep(TestUtils.getSleepTime());


        //Filter by tags
        onView(withId(R.id.sort_button)).perform(click()); // change name
        onView(withText(containsString("Filter"))).perform(click());
        onView(withText(containsString(originalTag1))).perform(click());
        onView(withText(containsString("APPLY"))).perform(click());


        // Verify that the sub shows up when the filter applied
        onView(withText("Name: " + originalName)).check(matches(withText("Name: " + originalName)));
        onView(withText("Payment Amount: $" + originalpayment + ".00")).check(matches(withText("Payment Amount: $" + originalpayment + ".00")));
        onView(withText("Frequency: " + originalFrequency)).check(matches(withText("Frequency: " + originalFrequency)));


        System.out.println("PASSED: filter by tags test!");
        SystemClock.sleep(TestUtils.getSleepTime());

    }


}