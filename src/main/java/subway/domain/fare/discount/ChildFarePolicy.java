package subway.domain.fare.discount;

import org.springframework.stereotype.Component;
import subway.domain.fare.Fare;

@Component
public class ChildFarePolicy implements DiscountFarePolicy {

    private static final int CHILD_DISCOUNT_RATE = 50;
    private static final Fare TAX = new Fare(350);

    @Override
    public Fare calculateFare(final Fare basicFare) {
        final Fare deductedFare = basicFare.subtract(TAX);
        final double discountPercentage = (100 - CHILD_DISCOUNT_RATE) * 0.01;
        return deductedFare.multiply(new Fare(discountPercentage));
    }
}
