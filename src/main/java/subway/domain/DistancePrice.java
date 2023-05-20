package subway.domain;

import subway.domain.vo.Distance;
import subway.domain.vo.Money;

import java.math.BigDecimal;

public class DistancePrice {

    private final Distance each;
    private final Money price;

    public DistancePrice(final Distance each, final Money price) {
        this.each = each;
        this.price = price;
    }

    public static DistancePrice from(final Integer each, final String price) {
        return new DistancePrice(new Distance(each), Money.from(price));
    }

    public Money impose(final Distance totalDistance) {
        final Distance quotient = totalDistance.quotient(each);
        return price.multiply(BigDecimal.valueOf(quotient.getValue()));
    }
}
