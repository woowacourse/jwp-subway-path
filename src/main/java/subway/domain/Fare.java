package subway.domain;

public class Fare {
    private static final int BASIC_RATE = 1250;
    private final long fare;

    public Fare(final long fare) {
        validate(fare);

        this.fare = fare;
    }

    private void validate(final long fare) {
        if (fare < BASIC_RATE) {
            throw new IllegalArgumentException("기본요금은 1250원부터 입니다.");
        }
    }

    public long getFare() {
        return fare;
    }
}
