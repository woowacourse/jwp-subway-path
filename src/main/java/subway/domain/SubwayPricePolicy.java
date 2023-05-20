package subway.domain;

public interface SubwayPricePolicy {

    int calculate(final Route route);
}
