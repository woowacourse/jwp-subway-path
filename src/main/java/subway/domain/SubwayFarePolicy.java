package subway.domain;

public interface SubwayFarePolicy {

    Money calculate(final Route route);
}
