package subway.domain.charge;

import java.util.List;
import subway.domain.WeightedEdgeWithLine;

public interface LineChargePolicy {
    Charge apply(List<WeightedEdgeWithLine> edges);
}
