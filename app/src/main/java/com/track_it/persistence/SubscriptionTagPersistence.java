package com.track_it.persistence;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;

import java.util.List;

public interface SubscriptionTagPersistence
{


    void addTagToPersistence(SubscriptionTag insertTag);

    void changeSubscriptionTags(SubscriptionObj inputSub);


     List<SubscriptionTag> getTagsForSubscription(SubscriptionObj inputSub);


    List<SubscriptionTag>  getAllTags();

    void removeAllTagsBySubID(int subID);



}
