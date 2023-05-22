package subway.domain;

import java.util.Objects;

public class Fare {

    private static final Fare ZERO = new Fare(0);
    private final int fare;

    private Fare(int fare) {
        validateFare(fare);
        this.fare = fare;
    }

    private void validateFare(int fare) {
        if (fare < 0) {
            throw new IllegalStateException("요금은 음수일 수 없습니다.");
        }
    }

    public static Fare from(int fare) {
        return new Fare(fare);
    }

    public static Fare zero() {
        return ZERO;
    }

    public Fare plus(int fare) {
        return new Fare(this.fare + fare);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "fare=" + fare +
                '}';
    }

    public int getFare() {
        return fare;
    }
}
