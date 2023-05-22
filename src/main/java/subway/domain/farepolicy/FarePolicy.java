package subway.domain.farepolicy;

import subway.domain.path.SubwayPath;

public interface FarePolicy {
    Fare calculate(final SubwayPath subwayPath);
}
