package com.track_it;


import com.track_it.application.SetupParameters;
import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.logic.SubscriptionFilter;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.SubscriptionTagHandler;
import com.track_it.logic.exceptions.SubscriptionTagException;
import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.SubscriptionTagPersistence;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;
import com.track_it.util.TestUtils;

import org.hsqldb.lib.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;


//This has been updated to use mockito mocks for the database to test unit.

public class UnitTagTest {

    private SubscriptionHandler subHandle;
    private SubscriptionTagPersistence tagDBMock;
    private SubscriptionPersistence subDBMock;


    private SubscriptionTagHandler tagHandler;

    //Setup a fake database each time
    @Before
    public void setTestHandle()
    {
        //Mock the databases
        tagDBMock = mock(SubscriptionTagPersistence.class);
        subDBMock = mock(SubscriptionPersistence.class);

        //Inject mocked database
        SetupParameters.initializeDatabase(subDBMock, tagDBMock);
        subHandle = SetupParameters.getSubscriptionHandler();
        tagHandler = subHandle.getTagHandler();
    }


    @Test
    public void addSubWithTagsTest() {


        int customID = 1;
        String tag1 = "tag_1";
        String tag2 = "tag_2";

        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazons", 104, subHandle.getFrequencyNameList().get(0));
        newSubToAdd.setID(customID);

        subHandle.setTags(newSubToAdd, tag1 + " " + tag2);
        subHandle.addSubscription(newSubToAdd);


        //Set mock rules
        when(tagDBMock.getTagsForSubscription(newSubToAdd)).thenReturn(newSubToAdd.getTagList());
        when(subDBMock.getSubscriptionByID(customID)).thenReturn(newSubToAdd);
        ArgumentCaptor<SubscriptionObj> argument = ArgumentCaptor.forClass(SubscriptionObj.class);
        verify(tagDBMock).changeSubscriptionTags(argument.capture());

        //Assert that the proper tags were saved
        SubscriptionObj tagsSavedForSub = argument.getValue();
        for (SubscriptionTag currtag : tagsSavedForSub.getTagList()) {
            assertTrue("FAILED add tags test: improper tags were saved to database", (currtag.getName().toLowerCase().equals(tag1) || currtag.getName().toLowerCase().equals(tag2)));
        }


        //Get the tags returned from logic
        List<SubscriptionTag> returnedTags = subHandle.getSubscriptionByID(newSubToAdd.getID()).getTagList();

        // Check that there are the correct amount of tags
        assertTrue("Failed addSubWithTagsTest: added a sub with 2 tags, and received a sub back with " + returnedTags.size() + " number of tags ", returnedTags.size() == 2);
        //Check that the subscription returned has the proper tags
        for (SubscriptionTag currTag : returnedTags) {
            String currTagName = currTag.getName();
            assertTrue("Failed addSubWithTagsTest: a sub was added with tags named " + tag1 + " " + tag2 + " but received " + currTagName.length(), currTagName.equals(tag1) || currTagName.equals(tag2));
        }

        System.out.println("PASSED: addSubWithTagsTest test ");

    }


    //Test adding tags
    @Test
    public void addTagsTest() {
        String tags = "tag1";
        List<SubscriptionTag> tagsAdded = tagHandler.stringToTags(tags);
        SubscriptionTag tagAdded = tagsAdded.get(0);


        //Set mock rules
        when(tagDBMock.getAllTags()).thenReturn(tagsAdded);

        tagHandler.addTag(tagAdded);
        List<SubscriptionTag> returnedTags = tagHandler.getAllSubTags();

        //Capture arguments and make sure they are correct
        ArgumentCaptor<SubscriptionTag> argument = ArgumentCaptor.forClass(SubscriptionTag.class);
        verify(tagDBMock).addTagToPersistence(argument.capture());
        when(tagDBMock.getAllTags()).thenReturn(tagsAdded);

        assertTrue("FAILED addTagsTest: Added " + tagsAdded.size() + " tags but got back " + returnedTags.size(), returnedTags.size() == tagsAdded.size());

        assertTrue("FAILED addTagsTest: Insert a tag with name " + tagAdded + " but got back a tag with name " + returnedTags.get(0), tagAdded.getName().equals(returnedTags.get(0).getName()));


        System.out.println("PASSED: addTagsTest");
    }

    @Test
    public void changeTagsForSubTest() {


        int customID = 1;
        String tag1 = "tag_1";
        String tag2 = "tag_2";

        String newtag1 = "new_tag_1";
        String newtag2 = "new_tag_2";

        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazons", 104, subHandle.getFrequencyNameList().get(0));
        newSubToAdd.setID(customID);

        subHandle.setTags(newSubToAdd, tag1 + " " + tag2);
        subHandle.addSubscription(newSubToAdd);


        //Set mock return rules
        when(tagDBMock.getTagsForSubscription(newSubToAdd)).thenReturn(newSubToAdd.getTagList());
        when(subDBMock.getSubscriptionByID(customID)).thenReturn(newSubToAdd);


        //Modify subscription
        SubscriptionObj subReturnedFromDatabase = subHandle.getSubscriptionByID(newSubToAdd.getID());
        subHandle.setTags(subReturnedFromDatabase, newtag1 + " " + newtag2);
        subHandle.editWholeSubscription(subReturnedFromDatabase.getID(), subReturnedFromDatabase);


        //Set mock return rules
        when(tagDBMock.getTagsForSubscription(subReturnedFromDatabase)).thenReturn(subReturnedFromDatabase.getTagList());
        when(subDBMock.getSubscriptionByID(customID)).thenReturn(subReturnedFromDatabase);

        //Capture argument
        ArgumentCaptor<SubscriptionObj> argument = ArgumentCaptor.forClass(SubscriptionObj.class);
        verify(tagDBMock, times(2)).changeSubscriptionTags(argument.capture());


        //Assert that the new proper tags were saved
        SubscriptionObj tagsSavedForSub = argument.getValue();
        for (SubscriptionTag currTag : tagsSavedForSub.getTagList()) {
            assertTrue("FAILED add tags test: improper tags were saved to database", (currTag.getName().toLowerCase().equals(newtag1) || currTag.getName().toLowerCase().equals(newtag2)));
        }


        // Get tags from logic layer
        List<SubscriptionTag> returnedTags = subHandle.getSubscriptionByID(newSubToAdd.getID()).getTagList();

        // Check that there are the correct amount of tags
        assertTrue("Failed changeTagsForSubTest: modified a sub with 2 tags, and received a sub back with " + returnedTags.size() + " number of tags ", returnedTags.size() == 2);
        // check that the tags names are correct
        for (SubscriptionTag currTag : returnedTags) {
            String currTagName = currTag.getName();
            assertTrue("Failed changeTagsForSubTest: a sub was modified with tags named " + tag1 + " " + tag2 + " but received " + currTagName, currTagName.equals(newtag1) || currTagName.equals(newtag2));
        }

        System.out.println("PASSED: changeTagsForSubTest test ");

    }


    //Add 2 different subs with tags, and then check that all the tags returned are correct
    @Test
    public void multipleSubsTagsTest() {


        //Tags we will add
        String[] addTags = {"tag_one", "tag_two", "tag_3", "tag_4", "tag_5"};

        //Used for mocking
        String stringOfAllTags = "";
        for (String currTagName : addTags) {
            stringOfAllTags += currTagName + "  ";
        }
        //Mock the database
        List<SubscriptionTag> allTheTagsWeAdded = subHandle.getTagHandler().stringToTags(stringOfAllTags);
        when(tagDBMock.getAllTags()).thenReturn(allTheTagsWeAdded);


        //Add a sub with 2 tags
        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazon", 1000, subHandle.getFrequencyNameList().get(0));
        subHandle.setTags(newSubToAdd, addTags[0] + " " + addTags[1]);
        subHandle.addSubscription(newSubToAdd);

        //Add another sub with 3 tags
        newSubToAdd = new SubscriptionObj("WareHouse", 300, subHandle.getFrequencyNameList().get(0));
        subHandle.setTags(newSubToAdd, addTags[2] + " " + addTags[3] + " " + addTags[4]);
        subHandle.addSubscription(newSubToAdd);


        // Get all tags
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
    public void removeTagTest() {
        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazon", 1000, subHandle.getFrequencyNameList().get(0));

        String[] addTags = {"tag_one", "tag_two"};
        subHandle.setTags(newSubToAdd, addTags[0] + " " + addTags[1]);
        subHandle.addSubscription(newSubToAdd);

        //Mock Database
        when(subDBMock.getSubscriptionByID(newSubToAdd.getID())).thenReturn(newSubToAdd);
        when(tagDBMock.getTagsForSubscription(newSubToAdd)).thenReturn(newSubToAdd.getTagList());


        SubscriptionObj subReturnedFromDatabase = subHandle.getSubscriptionByID(newSubToAdd.getID());

        subReturnedFromDatabase.setTagList(new ArrayList<SubscriptionTag>());
        subHandle.editWholeSubscription(subReturnedFromDatabase.getID(), subReturnedFromDatabase);


        //Get all return tags
        List<SubscriptionTag> returnedTags = subReturnedFromDatabase.getTagList();

        assertTrue("FAILED removeTagTest: failed to remove tags added to a subscription", returnedTags.size() == 0);


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
        subHandle.setTags(newSubToAdd, invalidTags);


        boolean exceptionThrown = false;
        try {
            subHandle.validateTagList(newSubToAdd.getTagList()); // try to validate list of subs
        } catch (SubscriptionTagException e) {
            exceptionThrown = true;
        }

        assertTrue("FAILED invalidTagsTes, tried to validate too many tags, but exception not thrown", exceptionThrown);


        System.out.println("PASSED: invalidTagTest test ");
    }


    //Test the match all and match any filters for tags
    @Test
    public void filterTagsTest() {

        List<SubscriptionTag> filterList;
        SubscriptionFilter subFilter = new SubscriptionFilter();
        String totalFilterTags;

        String name1 = "name1";
        int payment1 = 123;
        String frequency1 = subHandle.getFrequencyNameList().get(0);
        String tag1 = "one";
        String tag2 = "two";
        String totalTagsSub1 = tag1 + " " + tag2;

        SubscriptionObj sub1 = new SubscriptionObj(name1, payment1, frequency1);
        subHandle.setTags(sub1, tag1 + " " + tag2);


        String name2 = "name2";
        int payment2 = 123;
        String frequency2 = subHandle.getFrequencyNameList().get(0);
        String tag3 = "three";
        String totalTagsSub2 = tag1 + " " + tag3;

        SubscriptionObj sub2 = new SubscriptionObj(name2, payment2, frequency2);
        subHandle.setTags(sub2, tag1 + " " + tag3);


        totalFilterTags = tag1 + " " + tag2;
        filterList = subHandle.getTagHandler().stringToTags(tag1 + " " + tag2);


        assertTrue("FAILED filterTagsTest: sub was created with tags:' " + totalTagsSub1 + " ' but failed to match ANY tags in: ' " + totalFilterTags + " '", subFilter.checkIfSubHasAnyMatchingTags(sub1, filterList));
        assertTrue("FAILED filterTagsTest: sub was created with tags:' " + totalTagsSub2 + " ' but failed to match ANY tags in: ' " + totalFilterTags + " '", subFilter.checkIfSubHasAnyMatchingTags(sub2, filterList));
        assertTrue("FAILED filterTagsTest: sub was created with tags:' " + totalTagsSub1 + " ' but failed to match ALL tags in: ' " + totalFilterTags + " '", subFilter.checkIfSuHasAllTags(sub1, filterList));
        assertFalse("FAILED filterTagsTest: sub was created with tags:' " + totalTagsSub1 + " ' but matched ALL tags in: ' " + totalFilterTags + " '", subFilter.checkIfSuHasAllTags(sub2, filterList));


        System.out.println("PASSED filterTagsTest ");
    }

}
