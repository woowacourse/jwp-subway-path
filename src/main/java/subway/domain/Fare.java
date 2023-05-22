package subway.domain;

public class Fare {
    private static final int UNIT_FARE = 10;
    private final int value;

    public Fare(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
