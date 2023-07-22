package com.track_it;


import androidx.test.filters.LargeTest;

import com.track_it.application.Main;
import com.track_it.application.SetupParameters;
import com.track_it.presentation.SubscriptionTest;
import com.track_it.util.TestUtils;

import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@LargeTest
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SubscriptionTest.class
})

public class AllAcceptanceTests {

    //Set the database to be a real HSQL database with a test script - This runs before every class that is tested
    @ClassRule
    public static ExternalResource testRule = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            Main.setDBPathName("TestAcceptance");
        }
    };

}

