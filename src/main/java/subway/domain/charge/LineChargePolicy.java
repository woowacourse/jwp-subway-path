package subway.domain.charge;

import java.util.List;
import subway.domain.Charge;
import subway.domain.line.Line;

public interface LineChargePolicy {
    Charge apply(List<Line> linesInRoute);
}
