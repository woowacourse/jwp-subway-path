package subway.domain.fare;

import java.util.List;

import subway.domain.fare.strategy.discount.DiscountStrategy;
import subway.domain.fare.strategy.charge.ChargeStrategy;

public class FareCalculator {
    private static final int BASE_FARE = 1250;
    private final List<ChargeStrategy> chargeStrategies;
    private final List<DiscountStrategy> discountStrategies;

    public FareCalculator(List<ChargeStrategy> chargeStrategies, List<DiscountStrategy> discountStrategies) {
        this.chargeStrategies = chargeStrategies;
        this.discountStrategies = discountStrategies;
    }

    public int calculate(FareInfo fareInfo) {
        int fare = BASE_FARE;
        for (ChargeStrategy chargeStrategy : chargeStrategies) {
            fare += chargeStrategy.calculate(fareInfo);
        }

        for (DiscountStrategy discountStrategy : discountStrategies) {
            fare = discountStrategy.discount(fare, fareInfo);
        }
        return fare;
    }
}
