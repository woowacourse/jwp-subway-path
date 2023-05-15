package subway.service.domain;

public class Distance {

    private static final int LOWER_BOUND_INTEGER_INCLUSIVE = 0;

    private final int value;

    private Distance(int value) {
        this.value = value;
    }

    public static Distance from(int value) {
        validateDistance(value);

        return new Distance(value);
    }

    private static void validateDistance(int value) {

        if (value <= LOWER_BOUND_INTEGER_INCLUSIVE) {
            throw new IllegalArgumentException("거리는 정수만 가능합니다.");
        }
    }

    public int getValue() {
        return value;
    }

}
