package subway.domain.farepolicy;

import subway.domain.path.SubwayPath;

public interface OverFarePolicy {

    Fare calculateOverFare(final SubwayPath path);
}
