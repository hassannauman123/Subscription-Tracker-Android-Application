package com.track_it.logic.totalcost;

import com.track_it.domainobject.SubscriptionObj;
import java.util.List;

public class TotalCostCalculator implements SubscriptionCalculator {

    double totalCost = 0;
    private double yearlyCost = 0;
    private double weeklyCost = 0;
    private double monthlyCost = 0;
    private double dailyCost = 0;
    private final List<SubscriptionObj> listOfSubs;

    public TotalCostCalculator(List<SubscriptionObj> listOfSubs) {
        //get the data from db
        this.listOfSubs = listOfSubs;
    }

    @Override
    public void cost(List<SubscriptionObj> listOfSubs) {
        for (SubscriptionObj subscription : listOfSubs) {
            //get cost and paymentPeriod from db
            double subscriptionCost = subscription.getPaymentDollars();
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
    //return the cost
    @Override
    public double getYearlyCost() {
        return yearlyCost;
    }

    @Override
    public double getWeeklyCost() {
        return weeklyCost;
    }

    @Override
    public double getMonthlyCost() {
        return monthlyCost;
    }

    @Override
    public double getDailyCost() {
        return dailyCost;
    }
}
