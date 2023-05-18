package subway.domain;

public class Distance {

    int value;

    public Distance(int value) {
        this.value = value;
    }

    public boolean isLessThan(Distance distance) {
        return value < distance.value;
    }

    public boolean isLessAndEqualsThan(Distance distance) {
        return value <= distance.value;
    }

    public Distance substractOne() {
        return new Distance(value - 1);
    }

    public Distance substract(Distance distance) {
        return new Distance(value - distance.value);
    }

    public int divide(Distance distance) {
        return value / distance.value;
    }

    public int getValue() {
        return value;
    }
}
