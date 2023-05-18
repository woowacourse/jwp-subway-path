package subway.domain.fare;

public interface FarePolicy {

    Fare calculate(final int distance);
}
