package subway.application.discount;

import subway.domain.discount.Age;
import subway.domain.price.Price;

public interface AgeDiscountPolicy {
    Price DEFAULT_DEDUCTION = Price.from(350);

    Price calculate(Price price, Age age);

    boolean canDiscount(Age age);
}
