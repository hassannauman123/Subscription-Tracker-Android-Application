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
        IntegrationTagTests.class,
        SubscriptionFilterTest.class
}
)



public class AllIntegrationTests{


        //Set the database to be a real HSQL database with a test script - This runs before every class that is tested
        @ClassRule
        public static ExternalResource testRule = new ExternalResource(){
                @Override
                protected void before() throws Throwable
                {
                        TestUtils.setUseRealDatabase(true);
                        TestUtils.copyDB();

                };


        };

}



