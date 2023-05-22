package subway.application.discount;

import subway.domain.discount.Age;
import subway.domain.price.Price;

public class TeenagersDiscountPolicy implements AgeDiscountPolicy {
    private static final double TEENAGERS_DISCOUNT_RATE = 0.2;

    @Override
    public Price calculate(Price price, Age age) {
        if (canDiscount(age)) {
            return price.minus(DEFAULT_DEDUCTION).multiple(TEENAGERS_DISCOUNT_RATE);
        }
        return Price.ZERO;
    }

    @Override
    public boolean canDiscount(Age age) {
        return age.isTeenagers();
    }
}
