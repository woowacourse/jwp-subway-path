package subway.domain;

public interface FarePolicy {

    Fare calculate(Distance distance);
}
