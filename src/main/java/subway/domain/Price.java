package subway.domain;

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
        if (isAdult(age)) {
            return this;
        }
        final int basePrice = getPrice() - 350;
        if (isTeenager(age)) {
            return new Price((int) (basePrice * 0.8));
        }
        if (isChildren(age)) {
            return new Price((int) (basePrice * 0.5));
        }
        return new Price(0);
    }

    private static boolean isAdult(final int age) {
        return age >= 19;
    }

    private static boolean isTeenager(final int age) {
        return age >= 13;
    }

    private static boolean isChildren(final int age) {
        return age >= 6;
    }

    public int getPrice() {
        return price;
    }
}
