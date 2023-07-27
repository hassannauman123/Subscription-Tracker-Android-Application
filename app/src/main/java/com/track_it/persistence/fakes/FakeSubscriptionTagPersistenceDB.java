package com.track_it.persistence.fakes;

import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.persistence.SubscriptionTagPersistence;

import java.util.ArrayList;
import java.util.List;


//This is a fake database for the tags. It implements the SubscriptionTagPersistence interface
public class FakeSubscriptionTagPersistenceDB implements SubscriptionTagPersistence {
    private static List<SubscriptionTag> listOfTags = new ArrayList<SubscriptionTag>();
    private static List<int[]> tagSubAssociation = new ArrayList<int[]>();
    private static final int SUB_ID_INDEX = 0; //The index of the subID for an array in tagSubAssociation
    private static final int TAG_ID_INDEX = 1; //The index of the tagID for an array in tagSubAssociation


    private static int nextTagID = 0; // Do not ever reduce this number, even when removing from database!


    public void addTagToPersistence(SubscriptionTag tagToInsert) {

        if (!tagAlreadyInDatabase(tagToInsert)) // If it's not already in the database, add it
        {
            tagToInsert.setID(nextTagID);
            listOfTags.add(tagToInsert);
            nextTagID++;
        }
    }


    private boolean tagAlreadyInDatabase(SubscriptionTag tagToInsert) {
        boolean found = false;
        for (SubscriptionTag currTag : listOfTags) {
            if (currTag.getName().equals(tagToInsert.getName())) {
                found = true;
            }
        }
        return found;
    }


    public void changeSubscriptionTags(SubscriptionObj inputSub) {

        //Remove all the previous tag associations for the sub
        for (int i = 0; i < tagSubAssociation.size(); i++) {
            int[] currTagAssoc = tagSubAssociation.get(i);

            if (currTagAssoc[SUB_ID_INDEX] == inputSub.getID()) {
                tagSubAssociation.remove(currTagAssoc);
                i--;
            }
        }


        // Add all new tag associations
        for (SubscriptionTag currTag : inputSub.getTagList()) {

            if (!associationAlreadyExists(inputSub.getID(), currTag.getID()) || true) {

                addTagToPersistence(currTag); // Add to database if it wasn't already there
                int newAssociation[] = new int[2];
                newAssociation[SUB_ID_INDEX] = inputSub.getID();
                newAssociation[TAG_ID_INDEX] = currTag.getID();
                tagSubAssociation.add(newAssociation);
            }
        }


    }

    private boolean associationAlreadyExists(int inputSubID, int inputTagID) {
        boolean associationAlreadyExists = false;

        for (int[] currAssociation : tagSubAssociation) {
            if (currAssociation[SUB_ID_INDEX] == inputSubID && currAssociation[TAG_ID_INDEX] == inputTagID) {
                associationAlreadyExists = true;
            }
        }
        return associationAlreadyExists;
    }


    //Remove all tags that are not associated with a subscription
    public void removeUnusedTags() {


        for (int i = 0; i < listOfTags.size(); i++) {
            SubscriptionTag currTag = listOfTags.get(i);
            if (!tagFoundInAssociation(currTag)) {
                listOfTags.remove(currTag);
                i--;
            }
        }
    }

    private boolean tagFoundInAssociation(SubscriptionTag inputTag) {

        boolean foundInAssociation = false;

        for (int[] currAssoc : tagSubAssociation) {
            if (currAssoc[TAG_ID_INDEX] == inputTag.getID()) {
                foundInAssociation = true;
            }
        }

        return foundInAssociation;

    }

    public List<SubscriptionTag> getAllTags() {
        List<SubscriptionTag> listAllTags = new ArrayList<SubscriptionTag>();

        for (SubscriptionTag currTag : listOfTags) {
            //Return a copy, to make sure the object in database can't be modified elsewhere
            SubscriptionTag tagToReturn = new SubscriptionTag(currTag.getName());
            tagToReturn.setID(currTag.getID());

            listAllTags.add(tagToReturn);
        }

        return listAllTags;
    }


    public List<SubscriptionTag> getTagsForSubscription(SubscriptionObj inputSub) {

        List<SubscriptionTag> allTagsForSub = new ArrayList<SubscriptionTag>();

        for (int[] currAssociation : tagSubAssociation) {
            if (currAssociation[SUB_ID_INDEX] == inputSub.getID()) //There is a tag associated with this sub
            {

                // Find the tag that is associated with the sub, and add it to return list
                for (SubscriptionTag currTag : listOfTags) {
                    if (currTag.getID() == currAssociation[TAG_ID_INDEX]) {
                        //Return a copy, to make sure the object in database can't be modified elsewhere
                        SubscriptionTag tagToReturn = new SubscriptionTag(currTag.getName());
                        tagToReturn.setID(currTag.getID());

                        allTagsForSub.add(tagToReturn);
                    }
                }
            }
        }
        return allTagsForSub;
    }

    public void removeAllTagsBySubID(int subID) {

        for (int i = 0; i < tagSubAssociation.size(); i++) {
            int[] currAssociation = tagSubAssociation.get(i);
            if (currAssociation[SUB_ID_INDEX] == subID) {
                tagSubAssociation.remove(currAssociation);
                i--;
            }
        }
    }
}
