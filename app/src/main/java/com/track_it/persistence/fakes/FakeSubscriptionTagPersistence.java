package com.track_it.persistence.fakes;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.persistence.SubscriptionTagPersistence;

import java.util.ArrayList;
import java.util.List;

public class FakeSubscriptionTagPersistence implements SubscriptionTagPersistence
{
    private static List<SubscriptionTag> listOfTags = new ArrayList<SubscriptionTag>();
    private static int nextTagID = 0; // Do not ever reduce this number, even when removing from database!


    public void addTagToPersistence(SubscriptionTag tagToInsert)
    {
        tagToInsert.setID(nextTagID);
        listOfTags.add(tagToInsert);
        nextTagID++;
    }

    public void changeSubscriptionTags(SubscriptionObj inputSub)
    {}



    public List<SubscriptionTag> getTagsForSubscription(SubscriptionObj inputSub)
    {
        return null;
    }




}
