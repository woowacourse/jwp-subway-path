package subway.domain.fare.discount;

import org.springframework.stereotype.Component;
import subway.domain.fare.Fare;

@Component
public class TeenagerFarePolicy implements DiscountFarePolicy {

    private static final int TEENAGER_DISCOUNT_RATE = 20;
    private static final Fare TAX = new Fare(350);

    @Override
    public Fare calculateFare(final Fare basicFare) {
        final Fare deductedFare = basicFare.subtract(TAX);
        final double discountPercentage = (100 - TEENAGER_DISCOUNT_RATE) * 0.01;
        return deductedFare.multiply(new Fare(discountPercentage));
    }
}
