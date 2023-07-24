package com.track_it.acceptancetests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
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

public class AddTagsToSubs {


    //Details subscription
    private static final String originalName = "Park pass";
    private static final String originalPayment = "1600";
    private static final String originalFrequency = "monthly";
    private static final String originalTag1 = "original_tag1";
    private static final String originalTag2 = "original_tag2";
    private static final String newTag1 = "newtag1";
    private static final String newTag2 = "newtag2";


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


    // Create a subscription with tags, and then check that those tags show up in the subscription details.

    @Test
    public void addTagTest() {

        TestUtils.addSub(originalName, originalPayment, originalFrequency, originalTag1 + " " + originalTag2); //ADD SUB

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

    // Create a subscription with tags, and then check that those tags show up in the subscription details.
    @Test
    public void editTagsTest() {

        //Add subscription with certain tags
        TestUtils.addSub(originalName, originalPayment, originalFrequency, originalTag1 + " " + originalTag2);

        SystemClock.sleep(TestUtils.getSleepTime());

        //Go to sub details and change the tags of the sub
        onView(withText(containsString(originalName))).perform(click());
        SystemClock.sleep(TestUtils.getSleepTime()); // let page load
        onView(ViewMatchers.withId(R.id.details_edit_subscription)).perform(click()); // Click edit sub
        onView(withId(R.id.tag_input)).perform(replaceText(newTag1 + " " + newTag2)); // change tags

        SystemClock.sleep(TestUtils.getSleepTime());

        onView(withId(R.id.details_edit_subscription)).perform(click()); // Click save changes
        onView(withId(R.id.go_home)).perform(click()); // go home


        //Go back to sub details
        onView(withText(containsString(originalName))).perform(click());

        //Make sure new tags show up
        onView(withText(containsString(newTag1))).check(matches(isDisplayed()));
        onView(withText(containsString(newTag2))).check(matches(isDisplayed()));

        //Make sure old tags do not show up
        onView(withText(containsString(originalTag1))).check(doesNotExist());
        onView(withText(containsString(originalTag2))).check(doesNotExist());


        System.out.println("PASSED: add tags test!");
        SystemClock.sleep(TestUtils.getSleepTime());

    }


}