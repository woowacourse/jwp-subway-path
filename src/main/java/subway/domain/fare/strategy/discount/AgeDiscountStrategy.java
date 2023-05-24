package subway.domain.fare.strategy.discount;

import subway.domain.fare.FareInfo;

public class AgeDiscountStrategy implements DiscountStrategy {

    @Override
    public int discount(int fare, FareInfo fareInfo) {
        for (AgeDiscountPolicy policy : AgeDiscountPolicy.values()) {
            if (policy.getAgeLowerStandard() <= fareInfo.getAge()
                && fareInfo.getAge() < policy.getAgeUpperStandard()) {
                return (int)((fare - policy.getDiscountFare())
                    * (policy.getChargingRage()));
            }
        }
        return fare;
    }
}
