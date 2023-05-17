package subway.domain.fare;

public class Fare {

    private final int fare;

    public Fare(final int fare) {
        validate(fare);
        this.fare = fare;
    }

    private void validate(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("요금은 0보다 작을 수 없습니다.");
        }
    }
}
