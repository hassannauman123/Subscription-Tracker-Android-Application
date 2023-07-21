package com.track_it.domainobject;

public class SubscriptionTag
{

    private  int tagID;
    private String tagName;

    public SubscriptionTag( String inputName)
    {
       this.tagName = inputName ;
    }


    public String getName()
    {
        return tagName;
    }


    public void setID(int inputID)
    {
        this.tagID = inputID;
    }

    public int getID()
    {
        return this.tagID;
    }





}
