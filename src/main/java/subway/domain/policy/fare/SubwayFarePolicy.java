package subway.domain.policy.fare;

import java.util.List;
import subway.domain.Money;
import subway.domain.line.Line;
import subway.domain.station.Station;

public interface SubwayFarePolicy {

  Money calculate(final List<Line> lines, final Station departure, final Station arrival);
}
