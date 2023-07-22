package com.track_it.presentation;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.track_it.R;
import com.track_it.util.TestUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@LargeTest
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class SubscriptionTest {

    private final int sleepTime = 1000;

    private static String originalName = "Lawn Care";

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void addSubTest() {


        String name = originalName;
        String payment = "500";
        String frequency = "daily";

        onView(withId(R.id.add_subscription_button)).perform(click()); // Click add sub
        onView(withId(R.id.input_subscription_name)).perform(replaceText(name)); // Give name
        onView(withId(R.id.input_payment_amount)).perform(replaceText(payment)); // Give payment
        onView(withId(R.id.subscription_list)).perform(click()); // Give frequency
        onView(withText(frequency)).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.submit_sub_button)).perform(click()); // Click add, and go back to home back


        //SUB ADDED
        // This verifies the subscription has been added to page
        //Looks strange, but it will only work if the subscription shows up on main page with the exact correct text
        onView(withText("Name: " + name)).check(matches(withText("Name: " + name)));
        onView(withText("Payment Amount: $" + payment + ".00")).check(matches(withText("Payment Amount: $" + payment + ".00")));
        onView(withText("Frequency: " + frequency)).check(matches(withText("Frequency: " + frequency)));

        SystemClock.sleep(sleepTime);
        SystemClock.sleep(sleepTime);

        System.out.println("PASSED sub add Test!");


    }

    @Test
    public void modifySub() {

        onView(withText("Name: " + originalName)).perform(click());

        String newName = "Tinker town";
        String newPaymentAmount = "394.46";
        String newFrequency = "bi-weekly";


        //EDIT SUB
        onView(withId(R.id.details_edit_subscription)).perform(click()); // Click add sub
        onView(withId(R.id.detail_subscription_name)).perform(replaceText(newName)); // change name
        onView(withId(R.id.detail_subscription_amount)).perform(replaceText(newPaymentAmount)); // Change amount
        onView(withId(R.id.AutoComplete_drop_menu)).perform(click()); // Give new frequency
        onView(withText(newFrequency)).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        onView(withId(R.id.details_edit_subscription)).perform(click()); // Click edit sub
        onView(withId(R.id.go_home)).perform(click()); // go home
        SystemClock.sleep(sleepTime);

        //Verify that the sub details have saved
        onView(withText("Name: " + newName)).check(matches(withText("Name: " + newName)));
        onView(withText("Payment Amount: $" + newPaymentAmount)).check(matches(withText("Payment Amount: $" + newPaymentAmount)));
        onView(withText("Frequency: " + newFrequency)).check(matches(withText("Frequency: " + newFrequency)));

        SystemClock.sleep(sleepTime);
        originalName = newName;
        System.out.println("PASSED modify test!");
    }


    @Test
    public void removeSub() {
        onView(withText("Name: " + originalName)).perform(click());
        onView(withId(R.id.details_delete_subscription)).perform(click()); // Click delete sub
        onView(allOf(withId(android.R.id.button2), withText("Yes"), childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 2))).perform(click());


        //Verify that the sub has been deleted
        onView(withText("Name: " + originalName)).check(doesNotExist());


        System.out.println("PASSED remove test!");
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
