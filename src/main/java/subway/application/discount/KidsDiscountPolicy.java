package subway.application.discount;

import subway.domain.discount.Age;
import subway.domain.price.Price;

public class KidsDiscountPolicy implements AgeDiscountPolicy {
    private static final double KIDS_DISCOUNT_RATE = 0.5;

    @Override
    public Price calculate(Price price, Age age) {
        if (canDiscount(age)) {
            return price.minus(DEFAULT_DEDUCTION).multiple(KIDS_DISCOUNT_RATE);
        }
        return Price.ZERO;
    }

    @Override
    public boolean canDiscount(Age age) {
        return age.isKids();
    }
}