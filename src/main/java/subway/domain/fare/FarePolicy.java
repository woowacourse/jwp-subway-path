package subway.domain.fare;

import subway.domain.path.Path;

public interface FarePolicy {

    int calculate(final Path path, final Passenger passenger, final int fare);
}
