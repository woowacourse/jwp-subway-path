package subway.domain.fare.policy;

import subway.domain.fare.AgeGroup;
import subway.domain.fare.FareInformation;

public class AgeDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculate(int fare, final FareInformation fareInformation) {
        AgeGroup ageGroup = fareInformation.getAgeGroup();
        fare -= ageGroup.getDeductionAmount();
        return (int) (fare * (1 - ageGroup.getDiscountRate()));
    }
}
