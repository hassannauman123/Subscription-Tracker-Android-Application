package com.track_it.logic;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;

import java.util.List;


//Filter for Subscription object.
public class SubscriptionFilter
{


    //This function will check if inputSub has any of the tags in list inputTags.
    // Returns true if subscription has at least 1 of the tags in inputTags list, else returns false
    public boolean checkIfSubHasAnyMatchingTags(SubscriptionObj inputSub, List<SubscriptionTag> filterTags) {
        boolean hasTags = false;

        for (SubscriptionTag currTag : filterTags) //For all tags in filter tags
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


    //This function will check if inputSub has all of the tags in list inputTags.
    // Returns true if all filterTags have a match in inputSub .
    public boolean checkIfSuHasAllTags(SubscriptionObj inputSub, List<SubscriptionTag> filterTags) {

        boolean hasAllTags = true; // Does the subscription have all the filterTags?

        for (SubscriptionTag currFilterTag : filterTags ) //For all tags in  filterTags
        {
            boolean currFilterHasMatch = false; // Does currFilterTag have a match?

            for (SubscriptionTag currSubTag : inputSub.getTagList()) // for all tags in subscription
            {
                if (currFilterTag.getName().equals(currSubTag.getName()))
                {
                    currFilterHasMatch = true; //  the currFilterTag has a match
                }
            }

            hasAllTags = hasAllTags && currFilterHasMatch;
        }
        return hasAllTags;
    }

}
