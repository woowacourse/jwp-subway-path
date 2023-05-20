package subway.domain;

public interface SubwayChargePolicy {

    Money calculate(final Route route);
}
