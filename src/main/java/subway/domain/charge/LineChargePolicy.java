package subway.domain.charge;

import java.util.List;
import subway.domain.Line;

public interface LineChargePolicy {
    Charge apply(List<Line> linesInRoute);
}
