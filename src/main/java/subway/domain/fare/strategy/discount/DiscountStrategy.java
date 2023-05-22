package subway.domain.fare.strategy.discount;

import subway.domain.fare.FareInfo;

public interface DiscountStrategy {
    int discount(int fare, FareInfo fareInfo);
}
