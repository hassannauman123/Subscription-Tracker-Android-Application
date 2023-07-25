package com.track_it.logic.totalcost;
import com.track_it.domainobject.SubscriptionObj;
import java.util.List;
public interface SubscriptionCalculator {
    void cost(List<SubscriptionObj> listOfSubs);
    double getYearlyCost();
    double getWeeklyCost();
    double getMonthlyCost();
    double getDailyCost();
}
