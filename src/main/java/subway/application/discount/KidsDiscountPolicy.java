package subway.application.discount;

import subway.domain.discount.Age;
import subway.domain.price.Price;

public class KidsDiscountPolicy implements AgeDiscountPolicy {
    private static final double KIDS_DISCOUNT_RATE = 0.5;

    @Override
    public Price getDiscountPrice(Price price, Age age) {
        if (age.isKids()) {
            return price.minus(DEFAULT_DEDUCTION).multiple(KIDS_DISCOUNT_RATE);
        }
        return Price.ZERO;
    }
}
