package subway.application.fare;

import org.springframework.stereotype.Service;
import subway.domain.fare.Fare;
import subway.domain.path.SubwayPath;

@Service
public class FareCalculator {
    private static final Fare BASE_FARE = new Fare(1_250);

    private final ExtraFarePolicy extraFarePolicy;
    private final AgeDiscountPolicy ageDiscountPolicy;

    public FareCalculator(final ExtraFarePolicy extraFarePolicy, final AgeDiscountPolicy ageDiscountPolicy) {
        this.extraFarePolicy = extraFarePolicy;
        this.ageDiscountPolicy = ageDiscountPolicy;
    }

    public Fare calculatePathFare(final SubwayPath subwayPath, final int passengerAge) {
        final Fare extraFare = BASE_FARE.add(extraFarePolicy.calculateExtraFare(subwayPath));

        return ageDiscountPolicy.discount(extraFare, passengerAge);
    }
}
