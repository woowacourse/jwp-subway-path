package subway.domain.fare;

import subway.domain.path.Path;

public interface FarePolicy {

    int calculateFare(Path path);
}
