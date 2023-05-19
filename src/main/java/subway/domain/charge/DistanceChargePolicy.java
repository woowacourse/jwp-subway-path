package subway.domain.charge;

import subway.domain.Charge;
import subway.domain.Distance;

public interface DistanceChargePolicy {
    Charge apply(Distance totalDistance);
}
