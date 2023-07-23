package com.track_it;


import androidx.test.filters.LargeTest;

import com.track_it.acceptancetests.EditSubscriptionTest;
import com.track_it.acceptancetests.RemoveSubscriptionTest;
import com.track_it.acceptancetests.SortSubscriptionTest;
import com.track_it.acceptancetests.SubscriptionAddTest;
import com.track_it.acceptancetests.SubscriptionSearchTest;
import com.track_it.acceptancetests.TagsTest;
import com.track_it.application.Main;

import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@LargeTest
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SubscriptionAddTest.class,
        RemoveSubscriptionTest.class,
        EditSubscriptionTest.class,
        SubscriptionSearchTest.class,
        TagsTest.class,
        SortSubscriptionTest.class

})

public class AllAcceptanceTests {

    //Set the database to be a real HSQL database with a TestAcceptance script - This runs before every class that is tested
    @ClassRule
    public static ExternalResource testRule = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            Main.setDBPathName("TestAcceptance");

        }
    };

}

