package com.track_it;

import com.track_it.logic.SubscriptionHandler;
import com.track_it.persistence.FakeDataBase;
import com.track_it.presentation.util.SetupParameters;
import com.track_it.util.FillDataBase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

    SubscriptionsAddEditRemoveTest.class,
    SubscriptionValidateTest.class
}
)

public class AllTests
{


}

