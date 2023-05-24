package subway.domain.charge;

import java.util.List;
import subway.domain.vo.Charge;
import subway.domain.line.Line;

public interface LineChargePolicy {
    Charge apply(List<Line> linesInRoute);
}
