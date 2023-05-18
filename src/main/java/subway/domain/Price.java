package subway.domain;

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

    public Price plus(final Price price) {
        return new Price(this.getPrice() + price.getPrice());
    }

    public int getPrice() {
        return price;
    }
}
