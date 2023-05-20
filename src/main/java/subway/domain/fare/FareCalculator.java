package subway.domain.fare;

public class FareCalculator {

    private FarePolicy farePolicy;
    private DiscountPolicy discountPolicy;

    public FareCalculator(FarePolicy farePolicy, DiscountPolicy discountPolicy) {
        this.farePolicy = farePolicy;
        this.discountPolicy = discountPolicy;
    }

    public int calculate(final FareInformation fareInformation) {
        int fare = 0;
        fare += farePolicy.calculate(fareInformation);
        return discountPolicy.calculate(fare, fareInformation);
    }
}
