package subway.domain.fare.policy;

import subway.domain.fare.AgeGroup;
import subway.domain.fare.FareInformation;

public class AgeDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculate(int fare, FareInformation fareInformation) {
        AgeGroup ageGroup = fareInformation.getAgeGroup();
        return ageGroup.calculate(fare);
    }
}
