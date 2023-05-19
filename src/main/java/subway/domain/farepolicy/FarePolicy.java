package subway.domain.farepolicy;

import subway.domain.path.SubwayPath;

public interface FarePolicy {
    int calculate(final SubwayPath subwayPath);
}
