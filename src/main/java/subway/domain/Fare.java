package subway.domain;

public class Fare {
    private final long fare;

    public Fare(final long fare) {
        validate(fare);

        this.fare = fare;
    }

    private void validate(final long fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("요금은 0원 이상이여야 합니다.");
        }
    }

    public long getFare() {
        return fare;
    }
}
