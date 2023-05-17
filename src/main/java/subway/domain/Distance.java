package subway.domain;

public class Distance {

    private final int value;

    public Distance(int value) {
        this.value = value;
    }

    public Distance diff(Distance other) {
        int difference = Math.abs(value - other.value);
        return new Distance(difference);
    }

    public int getValue() {
        return value;
    }
}
