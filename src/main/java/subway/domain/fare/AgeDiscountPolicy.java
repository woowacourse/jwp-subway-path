package subway.domain.fare;

public class AgeDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculate(int fare, FareInformation fareInformation) {
        AgeGroup ageGroup = fareInformation.getAgeGroup();
        return ageGroup.calculate(fare);
    }
}
