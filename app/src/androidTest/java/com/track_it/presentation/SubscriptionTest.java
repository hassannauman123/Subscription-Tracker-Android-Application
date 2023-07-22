package com.track_it.presentation;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.track_it.R;
import com.track_it.application.SetupParameters;
import com.track_it.util.TestUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@LargeTest
@RunWith(AndroidJUnit4.class)

public class SubscriptionTest {


    private final int sleepTime = 300;

    private static final String originalName = "Lawn Care";
    private static final String originalpayment = "500";
    private static final String originalFrequency = "daily";
    private static final String originalTag1 = "customtag1";
    private static final String originalTag2 = "customtag2";


    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void testSetup() {
        //Clear the database and then repopulate the database with some sample subscriptions
        TestUtils.clearDatabase(SetupParameters.getSubscriptionHandler());
        TestUtils.populateDatabase(SetupParameters.getSubscriptionHandler());

        //We need the reload the activity, as it originally loaded with the database before the clear and populate methods happened.
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent refresh = new Intent(context, MainActivity.class);
        refresh.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(refresh);
    }

    @After
    public void testTearDown() {
        //Clear the database
        TestUtils.clearDatabase(SetupParameters.getSubscriptionHandler());
    }


    // Commonly used function, to add a sub starting from the main page, and then going back to home page
    private void addUser(String subName, String subPaymentAmount, String subFrequency, String subTags) {

        onView(withId(R.id.add_subscription_button)).perform(click()); // Click add sub - Starting from home page

        onView(withId(R.id.input_subscription_name)).perform(replaceText(subName)); // Give name
        onView(withId(R.id.input_payment_amount)).perform(replaceText(subPaymentAmount)); // Give payment
        onView(withId(R.id.subscription_list)).perform(click()); // Click frequency
        onView(withText(subFrequency)).inRoot(RootMatchers.isPlatformPopup()).perform(click());// Give frequency
        onView(withId(R.id.tag_input)).perform(replaceText(subTags)); // Give Tags

        onView(withId(R.id.submit_sub_button)).perform(click()); // Click add, and go back to home back

    }


    @Test
    public void addSubTest() {

        SystemClock.sleep(sleepTime);

        TestUtils.addUser(originalName, originalpayment, originalFrequency, "");

        // This verifies the subscription has been added.
        //Looks strange, but it will only work if the subscription shows up on main page with the exact correct text
        onView(withText("Name: " + originalName)).check(matches(withText("Name: " + originalName)));
        onView(withText("Payment Amount: $" + originalpayment + ".00")).check(matches(withText("Payment Amount: $" + originalpayment + ".00")));
        onView(withText("Frequency: " + originalFrequency)).check(matches(withText("Frequency: " + originalFrequency)));


        SystemClock.sleep(sleepTime);

        System.out.println("PASSED: sub add Test!");


    }


    @Test
    public void modifySub() {


        TestUtils.addUser(originalName, originalpayment, originalFrequency, ""); // Add the sub

        onView(withText("Name: " + originalName)).perform(click()); // Click on sub in main

        //New details that we will try to save
        String newName = "Tinker town";
        String newPaymentAmount = "394.46";
        String newFrequency = "bi-weekly";


        //EDIT SUB
        onView(withId(R.id.details_edit_subscription)).perform(click()); // Click edit sub

        SystemClock.sleep(sleepTime);// Sleep, just to more clearly see whats happening
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

        System.out.println("PASSED: modify test!");
        SystemClock.sleep(sleepTime);
    }


    @Test
    public void removeSub() {

        TestUtils.addUser(originalName, originalpayment, originalFrequency, ""); //ADD SUB

        SystemClock.sleep(sleepTime);


        //Delete sub
        onView(withText("Name: " + originalName)).perform(click());
        onView(withId(R.id.details_delete_subscription)).perform(click()); // Click delete sub
        //Click the yes button to confirm delete
        onView(allOf(withId(android.R.id.button2), withText("Yes"), TestUtils.childAtPosition(TestUtils.childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 2))).perform(click());


        //Verify that the sub has been deleted
        onView(withText("Name: " + originalName)).check(doesNotExist());


        System.out.println("PASSED: remove test!");
        SystemClock.sleep(sleepTime);

    }


    @Test
    public void addTagTest() {

        TestUtils.addUser(originalName, originalpayment, originalFrequency, originalTag1 + " " + originalTag2); //ADD SUB

        SystemClock.sleep(sleepTime);

        //Go to sub details
        onView(withText(containsString(originalName))).perform(click());
        SystemClock.sleep(sleepTime);

        //Make sure tags show up
        onView(withText(containsString(originalTag1))).check(matches(isDisplayed()));
        onView(withText(containsString(originalTag2))).check(matches(isDisplayed()));


        System.out.println("PASSED: add tags test!");
        SystemClock.sleep(sleepTime);

    }

    //@Test
    public void filterTagTest() {

        TestUtils.addUser(originalName, originalpayment, originalFrequency, originalTag1 + " " + originalTag2); //Add sub with tags

        SystemClock.sleep(sleepTime);


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
        SystemClock.sleep(sleepTime);

    }


    @Test
    public void searchTest() {


        String partialName = "cart";
        String inputName = "Streaming Cartoon Central";

        TestUtils.addUser(inputName, originalpayment, originalFrequency, originalTag1 + " " + originalTag2); //Add sub

        SystemClock.sleep(sleepTime);


        TestUtils.typeInSearch(partialName); // Search for part of the name


        // Verify that the sub shows up with the search applied
        onView(withText("Name: " + inputName)).check(matches(withText("Name: " + inputName)));


        System.out.println("PASSED: search test!");
        SystemClock.sleep(sleepTime);

    }


}
