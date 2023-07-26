package com.track_it.logic.totalcost;
import com.track_it.domainobject.SubscriptionObj;
import java.util.List;
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
