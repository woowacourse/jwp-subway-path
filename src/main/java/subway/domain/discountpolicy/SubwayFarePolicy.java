package subway.domain.discountpolicy;

import subway.domain.Fare;
import subway.domain.Line;

import java.util.List;

public class SubwayFarePolicy {

    private static final int BASIC_RATE = 1250;

    private final List<FarePolicy> farePolicies;

    public SubwayFarePolicy() {
        this.farePolicies = List.of(new BaseFarePolicy(), new DiscountPolicy());
    }

    public Fare calculateFare(final List<Line> boardingLines, final int distance, final int age) {
        int calculatedFare = BASIC_RATE;
        for (FarePolicy farePolicy : farePolicies) {
            calculatedFare = farePolicy.calculateFare(boardingLines, distance, age, calculatedFare);
        }
        return new Fare(calculatedFare);
    }
}
