package com.track_it.logic.totalcost;
import com.track_it.domainobject.SubscriptionObj;
import java.util.List;



//  Note*
// This interface was written by tian but the changes he made were copied and pasted here with his permission because the branch he was working on
// was behind the develop branch, and it caused some conflict issues when it was merged.

public interface SubscriptionCalculator {
    //calculator the cost
    public void cost(List<SubscriptionObj> listOfSubs);
    //get the number from calculator
    public int getYearlyCost();
    public int getYearlyCostInCents();
    public int getWeeklyCost();
    public int getWeeklyCostInCents();
    public int getMonthlyCost();
    public int getMonthlyCostInCents();
    public int getDailyCost();
    public int  getDailyCostInCents();
}
