package com.track_it.persistence;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;

import java.util.List;

public interface SubscriptionTagPersistence {


    void addTagToPersistence(SubscriptionTag insertTag); // Add tags to database

    void changeSubscriptionTags(SubscriptionObj inputSub); // Change the tags that a subscription has


    List<SubscriptionTag> getTagsForSubscription(SubscriptionObj inputSub); // Get tags for a subscription


    List<SubscriptionTag> getAllTags(); //Get all tags from database

    void removeAllTagsBySubID(int subID); //Remove all tags from a subscription

    void removeUnusedTags(); //Remove all tags that are not associated with a subscription


}
