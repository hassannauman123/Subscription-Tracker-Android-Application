package com.track_it;

import com.track_it.util.TestUtils;

import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

        SubscriptionsAddEditRemoveTest.class,
        SubscriptionValidateTest.class,
        SubscriptionSortTest.class,
        UnitTagTest.class
}
)

public class AllUnitTests
{


    //Set the database to be a Fake Database instance.
    @ClassRule
    public static ExternalResource testRule = new ExternalResource(){
        @Override
        protected void before() throws Throwable
        {
            TestUtils.setUseRealDatabase(false);
            TestUtils.copyDB();

        };


    };
}

