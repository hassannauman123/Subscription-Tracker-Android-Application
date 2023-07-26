package com.track_it.presentation;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.widget.EditText;

import com.track_it.R;
import com.track_it.application.SetupParameters;

import afu.org.checkerframework.checker.oigj.qual.O;


// This class is used to automatically set the text entered by a user into different colors.
public class TagColors {


    private static String[] tagColorList = null; // List of colors (tag colors will iterate through this list)

    //Set the text watch, such that the colors automatically changes as user types input.
    public static void setTextWatcher(Context context, EditText tagInput) {
        tagInput.addTextChangedListener(new TextWatcher() {


            @Override
            public void afterTextChanged(Editable s) {
                tagInput.removeTextChangedListener(this);
                int cursorPosition = tagInput.getSelectionStart(); //save cursor position
                TagColors.setTagColors(context, tagInput, tagInput.getText().toString()); //Set colors
                tagInput.setSelection(Math.min(tagInput.getText().length(), cursorPosition)); // Put the cursor back into correct position
                tagInput.addTextChangedListener(this);
            }


            //You have to have this dead code here, as abstract methods onTextChanged and afterTextChanged must be defined
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}


        });
    }


    //Set the input for a tag box input such that each tag in the string shows up as a different color
    public static void setTagColors(Context context, EditText tagInput, String input) {

        if (tagColorList == null)  //Initialize the string colors once
        {
            initColorList(context);
        }

        String[] getSplit = input.split(SetupParameters.getTagHandler().getSplitCriteria()); // Split up the input string into tags the


        String allTagsName = ""; // what we will set the tagInput text to be
        //Go through each tag, add them to allTagsName and give them a different color
        int index = 0;
        for (String currTagName : getSplit) {

            if ( ! currTagName.trim().equals("")) // Only add the word if it is not blank
            {
                allTagsName += "<span style='background:" + tagColorList[index % tagColorList.length] + "';>" + currTagName + "</span>"; // Each tag has a different color
                if (index + 1 < getSplit.length)
                {
                    allTagsName += " "; // Add a space between all words, except the last
                }
            }
            index++;
        }


        //If the last characters of the original string were blank spaces before, add them back now
        int endSpaceItr = input.length() - 1;
        while (endSpaceItr >= 0 && input.charAt(endSpaceItr) == ' ') {
            allTagsName += " ";
            endSpaceItr--;
        }


        tagInput.clearComposingText();
        tagInput.setText(Html.fromHtml(allTagsName)); //Set the text to the newly created one with proper colors

    }


    // Set up the color list, such that every other tag will have a different color
    private static void initColorList(Context context) {
        String[] tempList = {
                context.getResources().getString(R.string.tag_color1),
                context.getResources().getString(R.string.tag_color2),
                context.getResources().getString(R.string.tag_color3),
                context.getResources().getString(R.string.tag_color4),
                context.getResources().getString(R.string.tag_color5)
        };
        tagColorList = tempList;

    }


}
