package subway.domain.fare;

public class Fare {

    private static final int MIN_FARE = 1;
    private final int fare;

    public Fare(final int fare) {
        validatePositive(fare);
        this.fare = fare;
    }

    private void validatePositive(final int distance) {
        if (MIN_FARE > distance) {
            throw new IllegalArgumentException("요금은 1원 이상이어야 합니다.");
        }
    }

    public Fare addFare(final Fare other) {
        return new Fare(this.fare + other.fare);
    }

    public int getFare() {
        return fare;
    }
}
