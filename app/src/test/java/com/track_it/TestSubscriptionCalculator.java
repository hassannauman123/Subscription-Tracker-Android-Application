package com.track_it;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.totalcost.SubscriptionCalculator;
import com.track_it.logic.totalcost.TotalCostCalculator;


//  Note*
// This class was written by tian but was copied and pasted here with his permission because there were
// some conflicts with the develop branch when merged.
public class TestSubscriptionCalculator {
    private List<SubscriptionObj> listOfSubs;
    private SubscriptionCalculator costCalculator;
    @Before
    public void setup() {

        listOfSubs = new ArrayList<>();

        SubscriptionObj sub1 = new SubscriptionObj("ab", 10000, "yearly");


        listOfSubs.add(sub1);
        costCalculator = new TotalCostCalculator(listOfSubs);



    }
    @Test
    public void testDisplayCost() {
        // Call the method to be tested
        costCalculator.cost(listOfSubs);

        // Manually calculate the expected display strings using the mock subscription data
        int expectedYearlyCost = 100;
        int expectedYearlyCostIncents = 0;
        int expectedMonthlyCost = 8;
        int expectedMonthlyCostIncents = 33;

        // Get the actual displayed strings from the method (replace this part with your actual logic to access the displayed values)
        int yearlyCost = costCalculator.getYearlyCost();
        int yearlyCostIncents = costCalculator.getYearlyCostInCents();

        int monthlyCost = costCalculator.getMonthlyCost();
        int monthlyCostIncents = costCalculator.getMonthlyCostInCents();

        // Assert that the displayed strings match the expected strings
        assertEquals(expectedYearlyCost, yearlyCost);
        assertEquals(expectedYearlyCostIncents,  yearlyCostIncents);
        assertEquals(expectedMonthlyCost,  monthlyCost);
        assertEquals(expectedMonthlyCostIncents,  monthlyCostIncents);
    }
}
