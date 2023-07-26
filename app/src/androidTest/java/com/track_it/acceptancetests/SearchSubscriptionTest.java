package com.track_it.acceptancetests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.os.SystemClock;

import androidx.test.espresso.ViewInteraction;
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

public class SearchSubscriptionTest {


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
    public void testSetup()
    {
        TestUtils.clearDatabase(SetupParameters.getSubscriptionHandler());
        TestUtils.refreshPage();

    }

    @After
    public void testTearDown() {
        TestUtils.clearDatabase(SetupParameters.getSubscriptionHandler());
    }


    //Test the searching by name for a subscriptions.
    @Test
    public void searchTest() {

        //Add both subscriptions
        TestUtils.addSub(originalName, originalPayment, originalFrequency, originalTag1 + " " + originalTag2); //Add sub 1
        SystemClock.sleep(TestUtils.getSleepTime());

        TestUtils.addSub(originalName2, originalPayment2, originalFrequency2, originalTag1 + " " + originalTag2); //Add sub 2
        SystemClock.sleep(TestUtils.getSleepTime());


        onView(withContentDescription("Search")).perform(click()); // Click search button


        SystemClock.sleep(TestUtils.getSleepTime()); // We need this here to allow the search widget to load,
        SystemClock.sleep(TestUtils.getSleepTime());

        ViewInteraction searchAutoComplete = onView(allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")))); // Get search input target
        searchAutoComplete.perform(click()); // click it
        SystemClock.sleep(TestUtils.getSleepTime());
        searchAutoComplete.perform(replaceText(partialName), closeSoftKeyboard()); // put search string into into search input

        SystemClock.sleep(TestUtils.getSleepTime());


        // Verify that the first sub shows up with the search applied
        onView(withText("Name: " + originalName)).check(matches(withText("Name: " + originalName)));

        //Verify that the other sub does not show up
        onView(withText("Name: " + originalName2)).check(doesNotExist());


        System.out.println("PASSED: search test!");
        SystemClock.sleep(TestUtils.getSleepTime());

    }


}
