package subway.domain.pay;

import subway.domain.Distance;
import subway.domain.Money;

public class DistancePrice {

    private final Distance each;
    private final Money price;

    public DistancePrice(final Distance each, final Money price) {
        this.each = each;
        this.price = price;
    }

    public static DistancePrice from(final Integer each, final Double price) {
        return new DistancePrice(new Distance(each), Money.from(price));
    }

    public Money impose(final Distance totalDistance) {
        final Distance quotient = totalDistance.quotient(each);
        return price.multiply(quotient.getValue());
    }
}
