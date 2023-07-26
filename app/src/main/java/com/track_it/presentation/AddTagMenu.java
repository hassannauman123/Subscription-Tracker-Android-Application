package com.track_it.presentation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.track_it.R;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.logic.SubscriptionHandler;

import java.util.ArrayList;
import java.util.List;


// This class handles the presentation of the add subscription page for the app.
public class AddTagMenu {
    private List<SubscriptionTag> allTags; //List of all tags that already exist
    private List<SubscriptionTag> enteredTags; // list of tags that the user has entered so far


    //This is what the user will see when they click filter list by tags.
    //This is a pop up with each tag having a check box, and 2 buttons (apply and clear).
    public void showAddTagsMenu(Context context, SubscriptionHandler subHandler, EditText tagInput) {


        allTags = subHandler.getTagHandler().getAllSubTags(); //Get all tags that exist in the system
        enteredTags = subHandler.getTagHandler().stringToTags(tagInput.getText().toString()); //Get all the tags the user has entered


        boolean checkedArray[] = new boolean[allTags.size()];
        final String[] tagNameArray = new String[allTags.size()];

        //Construct the boolean array and string array that builder needs
        for (int i = 0; i < allTags.size(); i++) {
            for (int j = 0; j < enteredTags.size(); j++) {
                if (allTags.get(i).getName().equals(enteredTags.get(j).getName())) {
                    checkedArray[i] = true;
                }
            }
            tagNameArray[i] = allTags.get(i).getName();

        }


        //Create builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add existing Filter");

        //Set up the filter options as a series of check boxes for the user to click
        builder.setMultiChoiceItems(tagNameArray, checkedArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedArray[which] = isChecked;
            }
        });

        setAddTagsBehavior(builder, checkedArray, allTags, tagInput); //Set what happens when user clicks check boxes and buttons
        builder.show();
    }


    //Set the button behavior for the popup filter options
    private void setAddTagsBehavior(AlertDialog.Builder builder, boolean[] checkedArray, List<SubscriptionTag> addTagOptionList, EditText tagInput) {


        //What happens if the apply button is clicked
        builder.setPositiveButton("Apply Tags", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String tagsEnteredByUser = tagInput.getText().toString(); // What the user has enter for tax

                String totalTagsToShow = ""; // What we will change the text to

                // Iterate through the check boxes.
                // If the user has checked it, then add it to totalTagsToShow ( if it wasn't already in tagsEnteredByUser),
                // else try to remove it from tagsEnteredByUser. Combine the two strings at end

                for (int i = 0; i < checkedArray.length; i++) {
                    if (checkedArray[i])  //Is the checked box checked?
                    {
                        if (!tagsEnteredByUser.contains(addTagOptionList.get(i).getName()))  //Only add tag if it has not already been entered by user
                        {
                            totalTagsToShow += addTagOptionList.get(i).getName() + " "; // Add it
                        }
                    } else {
                        tagsEnteredByUser = tagsEnteredByUser.replace(addTagOptionList.get(i).getName(), ""); //Else try to remove it
                    }
                }

                totalTagsToShow = tagsEnteredByUser + " " + totalTagsToShow;
                tagInput.setText(totalTagsToShow); //Display that tags

            }
        });


    }
}

