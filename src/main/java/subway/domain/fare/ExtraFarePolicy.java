package subway.domain.fare;

import subway.domain.route.Path;

public interface ExtraFarePolicy {
    Fare calculateExtraFare(final Path path);
}
