package com.track_it;




import com.track_it.application.SetupParameters;
import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.util.TestUtils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;


public class SubscriptionTagTests {

    private SubscriptionHandler subHandle;


    //Setup a fake database each time
    @Before
    public void setTestHandle()
    {

        TestUtils.changeDatabase();
        subHandle =  SetupParameters.getSubscriptionHandler();

    }



    @Test
    public void addSubWithTagsTest()
    {
        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazon", 1000, subHandle.getFrequencyNameList().get(0));


        String tag1  = "tag_one";
        String tag2  = "tag_two";

        subHandle.setTags(newSubToAdd, tag1 + " " + tag2);
        subHandle.addSubscription(newSubToAdd);

        SubscriptionObj subReturnedFromDatabase = subHandle.getSubscriptionByID(newSubToAdd.getID());
        List<SubscriptionTag> returnedTags = subReturnedFromDatabase.getTagList();

        assertTrue("Failed tag test, added 2 tags, and received a sub back with " + returnedTags.size() + " number of tags " , returnedTags.size() == 2);



        for ( SubscriptionTag currTag: returnedTags)
        {
            String currTagName = currTag.getName();
            assertTrue (  "Failed tag test, a sub was added with tags named " + tag1 + " " + tag2 + " but received " + currTagName.length()  ,currTagName.equals(tag1) ||currTagName.equals(tag2)  );
        }


        System.out.println("PASSED: addSubWithTagsTest test ");

    }



    @Test
    public void addTagsMultipleSubCheckAllTags()
    {
        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazon", 1000, subHandle.getFrequencyNameList().get(0));




        String[] addTags = {"tag_one", "tag_two", "tag_3", "tag_4", "tag_5"};


        subHandle.setTags(newSubToAdd, addTags[0] + " " + addTags[1]);
        subHandle.addSubscription(newSubToAdd);

        //Create 1 sub with 3 tags
        String tag3  = "tag_one";
        String tag4  = "tag_two";
        String tag5  = "tag_two";


        newSubToAdd = new SubscriptionObj("WareHouse", 300, subHandle.getFrequencyNameList().get(0));
        subHandle.setTags(newSubToAdd, addTags[2] + " " + addTags[3] + " " + addTags[4]);
        subHandle.addSubscription(newSubToAdd);

        // Get all tags from database
        List<SubscriptionTag> returnedTags = subHandle.getTagHandler().getAllSubTags();

        assertTrue( "Failed tag test! Did not get back 5 tags when added across 2 different sub, " +returnedTags.size() + " was the returned size.", returnedTags.size() == addTags.length  );


        for ( SubscriptionTag currTag: returnedTags)
        {

            boolean match = false;
            for ( String tagAdded:  addTags )
            {
                if ( tagAdded.equals(currTag.getName()))
                {
                    match = true;
                }
            }

             assertTrue("Failed tag test!, added tags to sub, but got subs back from database that did not match any added tags", match);
        }


        System.out.println("PASSED: addTagsMultipleSubCheckAllTags test ");

    }

    @Test
    public void removeTagTag()
    {
        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazon", 1000, subHandle.getFrequencyNameList().get(0));




        String[] addTags = {"tag_one", "tag_two"};

        subHandle.setTags(newSubToAdd, addTags[0] + " " + addTags[1]);
        subHandle.addSubscription(newSubToAdd);

        SubscriptionObj subReturnedFromDatabase = subHandle.getSubscriptionByID(newSubToAdd.getID());
        subReturnedFromDatabase.setTagList(new ArrayList<SubscriptionTag>());
        subHandle.editWholeSubscription(subReturnedFromDatabase.getID(),subReturnedFromDatabase);

        //Get all return tags
        List<SubscriptionTag> returnedTags = subReturnedFromDatabase.getTagList();

        assertTrue("FAILED TAG TEST: failed to remove tags added to a subscription" , returnedTags.size() == 0);



        System.out.println("PASSED: removeTagTag test ");

    }

}
