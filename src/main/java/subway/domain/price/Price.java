package subway.domain.price;

public class Price {

    public static final int DEFAULT_PRICE = 1250;
    private final int price;

    private Price(final int price) {
        this.price = price;
    }

    public static Price from(final double distance) {
        return calculateByDistance(distance);
    }

    private static Price calculateByDistance(final double distance) {
        if (distance <= 10.0) {
            return new Price(DEFAULT_PRICE);
        }
        if (distance > 10.0 && distance <= 50.0) {
            final int count = (int) ((distance - 10) / 5) + 1;
            return new Price(DEFAULT_PRICE + count * 100);
        }
        final int count = (int) ((distance - 50) / 8) + 1;
        return new Price(DEFAULT_PRICE + 800 + count * 100);
    }

    public Price applyAge(final int age) {
        final Age ageGroup = Age.of(age);
        return new Price(ageGroup.calculatePrice(price));
    }

    public int getPrice() {
        return price;
    }
}
