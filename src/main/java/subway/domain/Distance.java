package subway.domain;

public class Distance {

    private final int value;

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("거리는 양의 정수여야 합니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
