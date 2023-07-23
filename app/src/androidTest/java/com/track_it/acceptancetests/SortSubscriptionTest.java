package com.track_it.acceptancetests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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

    //This test was auto create by espresso to test that the sort function works.
    @Test
    public void sortSubscriptionTest() {
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

        ViewInteraction materialTextView = onView(
                allOf(withId(android.R.id.title), withText("Sort A-Z"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.subscription_name), withText("Name: Amazon prime"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textView.check(matches(withText("Name: Amazon prime")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.subscription_name), withText("Name: Dark-Zone Pass"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textView2.check(matches(withText("Name: Dark-Zone Pass")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.subscription_name), withText("Name: Youtube"),
                        withParent(withParent(withId(R.id.subscription_list))),
                        isDisplayed()));
        textView3.check(matches(withText("Name: Youtube")));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.sort_button),
                        childAtPosition(
                                allOf(withId(R.id.main_page),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));


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
