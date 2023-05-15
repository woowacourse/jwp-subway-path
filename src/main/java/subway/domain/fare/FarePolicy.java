package subway.domain.fare;

public interface FarePolicy {

    int calculateOverFare(double distance);
}
