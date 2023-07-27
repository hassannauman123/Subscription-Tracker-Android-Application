package com.track_it.logic.totalcost;

import com.track_it.domainobject.SubscriptionObj;

import java.util.List;


//  Note*
// This class was written by tian but the changes he made were copied and pasted here with his permission because there were
// some small issues with the develop branch when he merged.

public class TotalCostCalculator implements SubscriptionCalculator {

    int totalCost = 0;
    private int yearlyCost = 0;
    private int weeklyCost = 0;
    private int monthlyCost = 0;
    private int dailyCost = 0;
    private final List<SubscriptionObj> listOfSubs;

    public TotalCostCalculator(List<SubscriptionObj> listOfSubs) {
        //get the data from db
        this.listOfSubs = listOfSubs;
    }

    @Override
    public void cost(List<SubscriptionObj> listOfSubs) {
        for (SubscriptionObj subscription : listOfSubs) {
            //get cost and paymentPeriod from db
            double subscriptionCost = subscription.getTotalPaymentInCents();

            String paymentPeriod = subscription.getPaymentFrequency();
            //calculate the cost

            if (paymentPeriod.equals("daily")) {
                totalCost += subscriptionCost * 365;
            } else if (paymentPeriod.equals("monthly")) {
                totalCost += subscriptionCost * 12;
            } else if (paymentPeriod.equals("weekly")) {
                totalCost += subscriptionCost * 52;
            } else if (paymentPeriod.equals("bi-weekly")) {
                totalCost += subscriptionCost * 26;
            } else if (paymentPeriod.equals("yearly")) {
                totalCost += subscriptionCost;
            }
        }

        dailyCost = totalCost / 365;
        weeklyCost = totalCost / 52;
        monthlyCost = totalCost / 12;
        yearlyCost = totalCost;

    }

    //return the cost in decimals and integers
    @Override
    public int getYearlyCost() {
        return yearlyCost / 100;
    }

    public int getYearlyCostInCents() {
        return yearlyCost % 100;
    }

    @Override
    public int getWeeklyCost() {
        return weeklyCost / 100;
    }

    public int getWeeklyCostInCents() {
        return weeklyCost % 100;
    }

    @Override
    public int getMonthlyCost() {
        return monthlyCost / 100;
    }

    public int getMonthlyCostInCents() {
        return monthlyCost - (monthlyCost / 100) * 100;
    }


    @Override
    public int getDailyCost() {
        return dailyCost / 100;
    }

    public int getDailyCostInCents() {
        return dailyCost - (dailyCost / 100) * 100;
    }
}
