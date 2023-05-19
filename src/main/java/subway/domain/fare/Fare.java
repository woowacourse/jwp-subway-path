package subway.domain.fare;

import subway.exception.InvalidFeeException;

public class Fare {

    private final int value;

    public Fare(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 0) {
            throw new InvalidFeeException("요금은 음수가 될 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
