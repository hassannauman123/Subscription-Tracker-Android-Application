package com.track_it.acceptancetests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.PositionAssertions.isAbove;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.track_it.R;
import com.track_it.presentation.MainActivity;
import com.track_it.util.TestUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SortSubscriptionTest {


    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void testSetup() {
        TestUtils.testSetup();
    }

    @After
    public void testTearDown() {
        TestUtils.testTearDown();
    }


    //This test was created with the help of espresso recorder to test that the sort by name feature works.
    @Test
    public void sortByNameSubscriptionTest() {


        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.sort_button),
                        childAtPosition(
                                allOf(withId(R.id.main_page),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());


        //Click the correct sort button
        ViewInteraction materialTextView = onView(
                allOf(withId(android.R.id.title), withText("Sort A-Z"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());


        //Make sure all the subscriptions still show up

        ViewInteraction textViewAmazon = onView(
                allOf(withId(R.id.subscription_name), withText("Name: Amazon prime"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textViewAmazon.check(matches(withText("Name: Amazon prime")));


        ViewInteraction textViewDark = onView(
                allOf(withId(R.id.subscription_name), withText("Name: Dark-Zone Pass"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textViewDark.check(matches(withText("Name: Dark-Zone Pass")));

        ViewInteraction textViewYoutube = onView(
                allOf(withId(R.id.subscription_name), withText("Name: Youtube"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textViewYoutube.check(matches(withText("Name: Youtube")));


        //Make sure the subscription show up in the correct order
        textViewAmazon.check(isCompletelyAbove(withText("Name: Dark-Zone Pass")));
        textViewDark.check(isCompletelyAbove(withText("Name: Youtube")));


    }

    //This test was auto created by espresso recorder to test that the sort by payment feature works.
    @Test
    public void sortByPaymentTest() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.sort_button),
                        childAtPosition(
                                allOf(withId(R.id.main_page),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());


        //Click the correct sort button
        ViewInteraction materialTextView = onView(
                allOf(withId(android.R.id.title), withText("Sort by Payment"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());


        //Make sure all the subscriptions still show up
        ViewInteraction textView10 = onView(
                allOf(withId(R.id.subscription_amount), withText("Payment Amount: $10.00"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textView10.check(matches(withText("Payment Amount: $10.00")));


        ViewInteraction textView1350 = onView(
                allOf(withId(R.id.subscription_amount), withText("Payment Amount: $13.50"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textView1350.check(matches(withText("Payment Amount: $13.50")));

        ViewInteraction textView1956 = onView(
                allOf(withId(R.id.subscription_amount), withText("Payment Amount: $19.56"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textView1956.check(matches(withText("Payment Amount: $19.56")));


        //Make sure the subscription show up in the correct order
        textView10.check(isCompletelyAbove(withText("Payment Amount: $13.50")));
        textView1350.check(isCompletelyAbove(withText("Payment Amount: $19.56")));

    }

    //This test was created with the help of espresso recorder to test that the sort by payment feature works.
    @Test
    public void sortByFrequencyTest() {


        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.sort_button),
                        childAtPosition(
                                allOf(withId(R.id.main_page),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        //Click the correct sort option
        ViewInteraction materialTextView = onView(
                allOf(withId(android.R.id.title), withText("Sort by frequency"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());


        //Target and make sure all the subscriptions show up
        ViewInteraction textViewDaily = onView(
                allOf(withId(R.id.subscription_frequency), withText("Frequency: daily"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textViewDaily.check(matches(withText("Frequency: daily")));

        ViewInteraction textViewMonthly = onView(
                allOf(withId(R.id.subscription_frequency), withText("Frequency: monthly"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textViewMonthly.check(matches(withText("Frequency: monthly")));

        ViewInteraction textViewYearly = onView(
                allOf(withId(R.id.subscription_frequency), withText("Frequency: yearly"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textViewYearly.check(matches(withText("Frequency: yearly")));


        //Make sure the subscription show up in the correct order
        textViewDaily.check(isCompletelyAbove(withText("Frequency: monthly")));
        textViewMonthly.check(isCompletelyAbove(withText("Frequency: yearly")));


    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
