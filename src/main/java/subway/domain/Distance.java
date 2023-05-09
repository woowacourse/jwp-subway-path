package subway.domain;

public class Distance {

    private static final int MIN = 1;
    private int value;

    public Distance(int value) {
        validatePositive(value);
        this.value = value;
    }

    private void validatePositive(int value) {
        if (value < MIN) {
            throw new IllegalArgumentException("거리는 양의 정수여야 합니다");
        }
    }
}
