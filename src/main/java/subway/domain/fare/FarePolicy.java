package subway.domain.fare;

public interface FarePolicy {

    FareAmount calculate(final int totalDistance);
}
