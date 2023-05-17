package subway.domain.fare;

import subway.domain.path.PathFindResult;

public interface FarePolicy {

    int calculate(final PathFindResult result, final Passenger passenger, final int fare);
}
