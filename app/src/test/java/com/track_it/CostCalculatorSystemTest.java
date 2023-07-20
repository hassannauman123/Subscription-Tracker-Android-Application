package com.track_it;
import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.totalcost.TotalCostCalculator;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

import android.util.Log;

public class CostCalculatorSystemTest {
    @Test
    public void testCostCalculation() {

        List<SubscriptionObj> listOfSubs = new ArrayList<>();
        SubscriptionObj dailySubscription = new SubscriptionObj("ab",100, "daily");
        SubscriptionObj weeklySubscription = new SubscriptionObj("bv",200, "weekly");
        SubscriptionObj biweeklySubscription = new SubscriptionObj("cv",400, "bi-weekly");
        SubscriptionObj monthlySubscription = new SubscriptionObj("jv",100, "monthly");
        SubscriptionObj yearlySubscription = new SubscriptionObj("cd",1000, "yearly");
        listOfSubs.add(dailySubscription);
        //listOfSubs.add(monthlySubscription);
        // listOfSubs.add(weeklySubscription);
        //listOfSubs.add(biweeklySubscription);

        TotalCostCalculator totalCostCalculator = new TotalCostCalculator(listOfSubs);


        totalCostCalculator.cost();


        double yearlyCost = totalCostCalculator.getYearlyCost();
        double weeklyCost = totalCostCalculator.getweeklyCost();
        double monthlyCost = totalCostCalculator.getmonthlyCost();
        double dailyCost = totalCostCalculator.getdailyCost();



        assertEquals(365, yearlyCost, 0.01);
        assertEquals(7.01, weeklyCost, 0.01);
        assertEquals(30.41, monthlyCost, 0.01);
        assertEquals(1, dailyCost, 0.01);

    }

}
