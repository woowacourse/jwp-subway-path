package subway.application.fare;

import org.springframework.stereotype.Component;
import subway.domain.fare.AgePassenger;
import subway.domain.fare.Fare;

@Component
public class AgeDiscountPolicy {

    private static final Fare DEDUCTION_FARE = new Fare(350);

    public Fare discount(final Fare originFare, final int age) {
        final double discountProportionByAge = AgePassenger.findDiscountProportionByAge(age);

        if (discountProportionByAge == 1) {
            return originFare;
        }

        final Fare subtractedFare = originFare.subtract(DEDUCTION_FARE);
        final int discountedFare = (int) (subtractedFare.getFare() * discountProportionByAge);
        return new Fare(discountedFare);
    }
}
