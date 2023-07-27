package com.track_it;


import com.track_it.application.SetupParameters;
import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.SubscriptionTagHandler;
import com.track_it.logic.exceptions.SubscriptionTagException;
import com.track_it.util.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;


public class IntegrationTagTests {

    private SubscriptionHandler subHandle;
    private SubscriptionTagHandler tagHandler;


    //Setup a fake database each time
    @Before
    public void setTestHandle() {

        TestUtils.changeDatabase();
        subHandle = SetupParameters.getSubscriptionHandler();
        tagHandler = subHandle.getTagHandler();
    }


    @Test
    public void addSubWithTagsTest() {


        subHandle = SetupParameters.getSubscriptionHandler();

        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazons", 104, subHandle.getFrequencyNameList().get(0));

        String tag1 = "tag_1";
        String tag2 = "tag_2";


        newSubToAdd.setTagList(subHandle.getTagHandler().stringToTags(tag1 + " " + tag2));
        subHandle.addSubscription(newSubToAdd);


        SubscriptionObj subReturnedFromDatabase = subHandle.getSubscriptionByID(newSubToAdd.getID());
        List<SubscriptionTag> returnedTags = subReturnedFromDatabase.getTagList();


        assertTrue("Failed addSubWithTagsTest: added a sub with 2 tags, and received a sub back with " + returnedTags.size() + " number of tags ", returnedTags.size() == 2);


        for (SubscriptionTag currTag : returnedTags) {
            String currTagName = currTag.getName();
            assertTrue("Failed addSubWithTagsTest: a sub was added with tags named " + tag1 + " " + tag2 + " but received " + currTagName.length(), currTagName.equals(tag1) || currTagName.equals(tag2));
        }


        System.out.println("PASSED: addSubWithTagsTest test ");

    }


    @Test
    public void multipleSubsTagsTest() {
        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazon", 1000, subHandle.getFrequencyNameList().get(0));


        String[] addTags = {"tag_one", "tag_two", "tag_3", "tag_4", "tag_5"};


        newSubToAdd.setTagList(subHandle.getTagHandler().stringToTags(addTags[0] + " " + addTags[1]));

        subHandle.addSubscription(newSubToAdd);

        //Create 1 sub with 3 tags
        String tag3 = "tag_one";
        String tag4 = "tag_two";
        String tag5 = "tag_two";


        newSubToAdd = new SubscriptionObj("WareHouse", 300, subHandle.getFrequencyNameList().get(0));
        newSubToAdd.setTagList(subHandle.getTagHandler().stringToTags(addTags[2] + " " + addTags[3] + " " + addTags[4]));
        subHandle.addSubscription(newSubToAdd);

        // Get all tags from database
        List<SubscriptionTag> returnedTags = subHandle.getTagHandler().getAllSubTags();

        assertTrue("Failed tag test! Did not get back 5 tags when added across 2 different sub, " + returnedTags.size() + " was the returned size.", returnedTags.size() == addTags.length);


        for (SubscriptionTag currTag : returnedTags) {

            boolean match = false;
            for (String tagAdded : addTags) {
                if (tagAdded.equals(currTag.getName())) {
                    match = true;
                }
            }

            assertTrue("Failed multipleSubsTagsTest: added tags to subs, but got subs back from database that did not match any added tags", match);
        }


        System.out.println("PASSED: multipleSubsTagsTest test ");

    }

    @Test
    public void removeTagTag() {
        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazon", 1000, subHandle.getFrequencyNameList().get(0));


        String[] addTags = {"tag_one", "tag_two"};

        newSubToAdd.setTagList(subHandle.getTagHandler().stringToTags(addTags[0] + " " + addTags[1]));


        subHandle.addSubscription(newSubToAdd);

        SubscriptionObj subReturnedFromDatabase = subHandle.getSubscriptionByID(newSubToAdd.getID());
        subReturnedFromDatabase.setTagList(new ArrayList<SubscriptionTag>());
        subHandle.editWholeSubscription(subReturnedFromDatabase.getID(), subReturnedFromDatabase);

        //Get all return tags
        List<SubscriptionTag> returnedTags = subReturnedFromDatabase.getTagList();

        assertTrue("FAILED removeTagTag test: failed to remove tags added to a subscription", returnedTags.size() == 0);


        System.out.println("PASSED: removeTagTag test ");

    }

    @Test
    public void invalidTagTest() {


        String invalidTags = "";

        for (int i = 0; i < subHandle.getMaxTags() + 1; i++) {
            invalidTags += "tag" + i + " ";
        }

        // Add 1 too many tags to sub
        SubscriptionObj newSubToAdd = new SubscriptionObj("WareHouse", 300, subHandle.getFrequencyNameList().get(0));
        newSubToAdd.setTagList(subHandle.getTagHandler().stringToTags(invalidTags));


        boolean exceptionThrown = false;
        try {
            subHandle.validateTagList(newSubToAdd.getTagList()); // try to validate list of subs
        } catch (SubscriptionTagException e) {
            exceptionThrown = true;
        }

        assertTrue("FAILED invalidTagsTes, tried to validate too many tags, but exception not thrown", exceptionThrown);


        System.out.println("PASSED: invalidTagTest test ");
    }


    @Test
    public void addTagsTest() {
        String tags = "tag1 tag2 tag3";
        List<SubscriptionTag> tagsAdded = tagHandler.stringToTags(tags);
        List<SubscriptionTag> returnedTags;

        for (SubscriptionTag currTag : tagsAdded) // Add all the tags
        {
            tagHandler.addTag(currTag);
        }

        returnedTags = tagHandler.getAllSubTags(); // Get all the tags


        //Make sure there are the same number of tags returned as added
        assertTrue("FAILED addTagsTest: Added " + tagsAdded.size() + " tags but got back " + returnedTags.size(), returnedTags.size() == tagsAdded.size());


        for (SubscriptionTag addedTag : tagsAdded) {
            boolean foundMatch = false;
            for (SubscriptionTag returnedTag : returnedTags) {
                if (addedTag.getName().equals(returnedTag.getName())) {
                    foundMatch = true; // All tags added should have been returned
                }

            }
            assertTrue("FAILED addTagsTest: Added " + addedTag.getName() + " but did not get a tag returned with that name", foundMatch);

        }


        System.out.println("PASSED: addTagsTest");
    }


}
