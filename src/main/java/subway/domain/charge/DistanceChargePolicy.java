package subway.domain.charge;

import subway.domain.vo.Charge;
import subway.domain.vo.Distance;

public interface DistanceChargePolicy {
    Charge apply(Distance totalDistance);
}
