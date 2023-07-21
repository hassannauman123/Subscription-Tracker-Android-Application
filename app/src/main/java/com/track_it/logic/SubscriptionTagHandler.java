package com.track_it.logic;


import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.logic.exceptions.SubscriptionTagException;
import com.track_it.persistence.SubscriptionTagPersistence;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionTagHandler
{

    private final int MIN_NAME_TAG_LENGTH ;
    private final int MAX_NAME_TAG_LENGTH ;
    private final String TAG_SPLIT_CRITERIA;

    private final SubscriptionTagPersistence subscriptionTagPersistence;

    public SubscriptionTagHandler(String inputTagSplitCriteria, int  inputMinNameLength , int inputMaxNameLength, SubscriptionTagPersistence inputDatabase)
    {
        subscriptionTagPersistence = inputDatabase;
        TAG_SPLIT_CRITERIA =   inputTagSplitCriteria;
        MIN_NAME_TAG_LENGTH = inputMinNameLength;
        MAX_NAME_TAG_LENGTH =  inputMaxNameLength;
    }


    public void changeSubTags( SubscriptionObj inputSub)
    {
        this.subscriptionTagPersistence.changeSubscriptionTags(inputSub);
    }



    public void addTag(SubscriptionTag tagToAdd)
    {
        this.subscriptionTagPersistence.addTagToPersistence(tagToAdd);
    }

    public List<SubscriptionTag> getTagsForSubscription(SubscriptionObj inputSub)
    {
       return  subscriptionTagPersistence.getTagsForSubscription(inputSub);
    }


    //
    // Please double check that throwing a void validate method is okay?
    //
    public void validateTagString(String inputTagString) throws SubscriptionTagException
    {
        String[] splitTagString = inputTagString.split(TAG_SPLIT_CRITERIA);

        for ( String currName : splitTagString )
        {
            validateTagName(currName);
        }
    }

    public void validateTagName(String inputName ) throws SubscriptionTagException
    {

        if (inputName == null)
        {
            throw new SubscriptionTagException("A tag was invalid!");
        }
        else if ( inputName.length() < MIN_NAME_TAG_LENGTH)
        {
            throw new SubscriptionTagException(" tag was too short!");
        }
        else if ( inputName.length() > MAX_NAME_TAG_LENGTH)
        {
            throw new SubscriptionTagException( " tag was too long! max "+ MAX_NAME_TAG_LENGTH  +" chars long" );
        }
    }


    public String getSplitCriteria()
    {
        return TAG_SPLIT_CRITERIA;
    }


    public List<SubscriptionTag> stringToTags(String inputTagString)  throws SubscriptionTagException {
        List<SubscriptionTag> tagList = new ArrayList<SubscriptionTag>();
        String splitTagString[] = inputTagString.split(TAG_SPLIT_CRITERIA);

        for (String newTag : splitTagString) {
            if (!newTag.trim().equals("")) // Only bother adding non blank tags
            {
                validateTagName(newTag);
                tagList.add(new SubscriptionTag((newTag)));
            }
        }



        return tagList;
    }

}
