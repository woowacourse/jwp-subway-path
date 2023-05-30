package subway.domain.price;

import subway.domain.Distance;

import java.util.Objects;

public class Price {

    private static final int ZERO = 0;
    private final int price;

    private Price(final int price) {
        validate(price);
        this.price = price;
    }

    public static Price from(final Double price) {
        return new Price(price.intValue());
    }

    public static Price from(final int price) {
        return new Price(price);
    }

    private void validate(final int price) {
        if (price < ZERO) {
            throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
        }
    }

    public static Price calculateSurcharge(final Distance originDistance,
                                           final Distance farePerDistance,
                                           final Price additionFare) {
        final int originValue = originDistance.getDistance();
        final int perFareValue = farePerDistance.getDistance();

        final int resultValue = ((originValue - 1) / perFareValue + 1) * additionFare.price;

        return Price.from(resultValue);
    }

    public Price plus(final Price price) {
        return new Price(this.getPrice() + price.getPrice());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Price price1 = (Price) o;
        return price == price1.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public int getPrice() {
        return price;
    }
}
