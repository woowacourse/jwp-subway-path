package subway.domain.policy.discount;

public class AgeDiscountPolicy implements DiscountPolicy {

    private final AgeGroup ageGroup;

    private AgeDiscountPolicy(int age) {
        this.ageGroup = AgeGroup.of(age);
    }

    public static AgeDiscountPolicy from(int age) {
        return new AgeDiscountPolicy(age);
    }

    @Override
    public int calculate(int fare) {
        return ageGroup.calculate(fare);
    }
}
