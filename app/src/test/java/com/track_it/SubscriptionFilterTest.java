package com.track_it;


import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.logic.SubscriptionFilter;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.SubscriptionTagHandler;
import com.track_it.application.SetupParameters;
import com.track_it.util.TestUtils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;


public class SubscriptionFilterTest {

    private SubscriptionHandler subHandler;
    private SubscriptionTagHandler tagHandler;


    //Setup the database each time
    @Before
    public void setTestHandle() {
        TestUtils.changeDatabase();
        subHandler = SetupParameters.getSubscriptionHandler();
        tagHandler = subHandler.getTagHandler();
    }


    //Test adding subscriptions with tag lists, and then see if filter class properly filters the subscription.
    // This test has no mocks, as SubscriptionFilter has no dependencies and does not talk to database.
    @Test
    public void tagFilterTest() {
        SubscriptionFilter subFilter = new SubscriptionFilter();


        //Make two subs with tags ( sub1 has tags -> one, two     sub2 has tag -> twom three

        //Sub 1 - create sub wth tag1 and tag2
        SubscriptionTag tag1 = new SubscriptionTag("one");
        SubscriptionTag tag2 = new SubscriptionTag("two");
        List<SubscriptionTag> tagList1 = new ArrayList<SubscriptionTag>();
        tagList1.add(tag1);
        tagList1.add(tag2);

        SubscriptionObj sub1 = new SubscriptionObj("name", 22,"daily");
        sub1.setTagList(tagList1);


        //Sub2 - create a sub with tag2 and tag3
        SubscriptionTag tag3 = new SubscriptionTag("three");
        List<SubscriptionTag> tagList2 = new ArrayList<SubscriptionTag>();
        tagList2.add(tag3);
        tagList2.add(tag2);

        SubscriptionObj sub2 = new SubscriptionObj("name", 22,"daily");
        sub2.setTagList(tagList2);

        List<SubscriptionTag> tagsCheck = new ArrayList<SubscriptionTag>();
        tagsCheck.add(tag1);


        assertTrue("FAILED tagFilterTest, created a sub with tags named '" + tag1.getName() + "' and '" + tag2.getName() + "' But didn't match any tag with name: " + tagsCheck.get(0).getName(), subFilter.checkIfSubHasAnyMatchingTags(sub1, tagsCheck));
        assertFalse("FAILED tagFilterTest, created a sub with tags named '" + tag2.getName() + "' and  '" + tag3.getName() + "' But matched with tag named: " + tagsCheck.get(0).getName(), subFilter.checkIfSubHasAnyMatchingTags(sub2, tagsCheck));

        tagsCheck.add(tag3);
        assertFalse("FAILED tagFilterTest, created a sub with tags" + tag1.getName() + " " + tag2.getName() + "But ALL matched with" + tagsCheck.get(0).getName() + " and " + tagsCheck.get(1).getName(), subFilter.checkIfSuHasAllTags(sub1, tagsCheck));


        System.out.println("PASSED tagFilterTest");


    }
}