package com.track_it.acceptancetests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
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

import android.content.Context;
import android.os.SystemClock;

import androidx.test.core.app.ApplicationProvider;
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

public class FilterByTagsTest {


    //Details for a subscription
    private static final String originalName = "Park pass";
    private static final String originalPayment = "1600";
    private static final String originalFrequency = "monthly";
    private static final String originalTag1 = "tag1";
    private static final String originalTag2 = "tag2";

    //Details for anther subscription
    private static final String originalName2 = "Costco membership";
    private static final String originalPayment2 = "1000";
    private static final String originalFrequency2 = "yearly";
    private static final String originalTag3 = "tag3";
    private static final String originalTag4 = "tag4";



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



    // Create 2 subscriptions with different tags, then filter by tags such that only the first subscription should be displayed.
    // verify that this happens.
    @Test
    public void filterTagTest() {

        // create 2 subs, but with different tags
        TestUtils.addSub(originalName, originalPayment, originalFrequency, originalTag1 + " " + originalTag2); //Add sub with tags
        SystemClock.sleep(TestUtils.getSleepTime());
        TestUtils.addSub(originalName2, originalPayment2, originalFrequency2, originalTag3 + " " + originalTag3 + " " + originalTag1); //Add sub with tags


        SystemClock.sleep(TestUtils.getSleepTime());

        Context currContext = ApplicationProvider.getApplicationContext(); // Get current context so we can use string resources
        String filterButtonName = currContext.getResources().getString(R.string.filter_option_title); // The current sort button will have a text of this
        String applyFilterName = currContext.getResources().getString(R.string.apply_filter_confirmation); // The current sort button will have a text of this


        //Filter by tags
        onView(withId(R.id.sort_button)).perform(click());
        SystemClock.sleep(TestUtils.getSleepTime()); //Let popup load
        onView(withText(containsString(filterButtonName))).perform(click()); // click filter button option
        SystemClock.sleep(TestUtils.getSleepTime()); // Wait for menu to load
        onView(withText(containsString(originalTag1))).perform(click()); // Check filter to use
        onView(withText(containsString(originalTag2))).perform(click()); // Check filter to use
        onView(withText(containsString(applyFilterName))).perform(click()); // click apply button


        // Verify that the sub with the correct tags shows up when the filter applied
        onView(withText(containsString( originalName))).check(matches(withText(containsString(originalName))));
        onView(withText(containsString(originalFrequency))).check(matches(withText(containsString(originalFrequency))));

        //Verify that the sub without the tags does NOT show up
        onView(withText(containsString(originalName2))).check(doesNotExist());


        SystemClock.sleep(TestUtils.getSleepTime());
        //Filter by matching any tag
        onView(withId(R.id.fitler_match_any_switch)).perform(click());


        // Verify that both now show up
        onView(withText(containsString( originalName))).check(matches(withText(containsString(originalName))));
        onView(withText(containsString(originalFrequency))).check(matches(withText(containsString(originalFrequency))));

        onView(withText(containsString(originalName2))).check(matches(isDisplayed()));




        System.out.println("PASSED: filter by tags test!");
        SystemClock.sleep(TestUtils.getSleepTime());

    }


}