package subway.domain;

public interface FarePolicy {

    Fare calculate(int distance);
}
