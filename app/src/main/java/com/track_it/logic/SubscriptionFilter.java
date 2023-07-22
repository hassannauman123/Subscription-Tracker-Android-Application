package com.track_it.logic;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;

import java.util.List;


//Filter for Subscription object.
public class SubscriptionFilter
{


    //This function will check if inputSub has any of the tags in list inputTags.
    // Returns true if subscription has at least 1 of the tags in inputTags list, else returns false
    public boolean checkIfSubHasTags(SubscriptionObj inputSub, List<SubscriptionTag> inputTags) {
        boolean hasTags = false;

        for (SubscriptionTag currTag : inputTags) //For all tags in input tags
        {
            for (SubscriptionTag currSubTag : inputSub.getTagList()) // for all tags in inputSubscription
            {

                if (currSubTag.getName().equals(currTag.getName())) // Check if tags are the same
                {
                    hasTags = true; // This sub has at least one tag from inputTags
                }
            }
        }
        return hasTags;
    }

}
