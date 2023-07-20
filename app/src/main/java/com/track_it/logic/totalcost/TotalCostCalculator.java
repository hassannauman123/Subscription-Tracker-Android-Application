package com.track_it.logic.totalcost;
import com.track_it.domainobject.SubscriptionObj;
import java.util.List;
public class TotalCostCalculator {


        double totalCost = 0;
        private double yearlyCost = 0;
        private double weeklyCost=0;
        private double monthlyCost=0;
        private double dailyCost=0;
        private final List<SubscriptionObj> listOfSubs;

        public TotalCostCalculator(List<SubscriptionObj> listOfSubs) {
            this.listOfSubs = listOfSubs;
        }

        public void cost() {

            for (SubscriptionObj subscription : listOfSubs) {
                double subscriptionCost = subscription.getPaymentDollars();
                String paymentPeriod = subscription.getPaymentFrequency();

                if (paymentPeriod.equals("daily")) {

                    totalCost += subscriptionCost * 365;
                } else if (paymentPeriod.equals("monthly")) {

                    totalCost += subscriptionCost * 12;
                }else if(paymentPeriod.equals("weekly")){
                    totalCost +=subscriptionCost * 52;
                }else if(paymentPeriod.equals("bi-weekly")){
                    totalCost +=subscriptionCost * 26;
                } else if (paymentPeriod.equals("yearly")) {

                    totalCost += subscriptionCost;
                }
            }


             dailyCost = totalCost / 365;
             weeklyCost = totalCost/ 52;
             monthlyCost = totalCost / 12;
             yearlyCost = totalCost;

        }


    public double getYearlyCost() {
            return yearlyCost;
        }
    public double getweeklyCost() {
        return weeklyCost;
    }
    public double getmonthlyCost() {
        return monthlyCost;
    }
    public double getdailyCost() {
        return dailyCost;
    }
    }


