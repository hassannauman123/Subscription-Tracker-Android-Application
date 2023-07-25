package com.track_it;


import androidx.test.filters.LargeTest;

import com.track_it.acceptancetests.EditSubscriptionTest;
import com.track_it.acceptancetests.FilterByTagsTest;
import com.track_it.acceptancetests.RemoveSubscriptionTest;
import com.track_it.acceptancetests.SortSubscriptionTest;
import com.track_it.acceptancetests.AddSubscriptionTest;
import com.track_it.acceptancetests.SearchSubscriptionTest;
import com.track_it.acceptancetests.AddTagsToSubs;
import com.track_it.application.Main;

import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.logging.Filter;

@LargeTest
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AddSubscriptionTest.class,
        RemoveSubscriptionTest.class,
        EditSubscriptionTest.class,
        SearchSubscriptionTest.class,
        AddTagsToSubs.class,
        FilterByTagsTest.class,
        SortSubscriptionTest.class

})

public class AllAcceptanceTests {

    //Set the database to be a real HSQL database with a TestAcceptance script
    @ClassRule
    public static ExternalResource testRule = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            Main.setDBPathName("TestAcceptance");

        }
    };

}

