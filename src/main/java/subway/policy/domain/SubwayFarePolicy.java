package subway.policy.domain;

import java.util.List;
import subway.value_object.Money;
import subway.line.domain.Line;
import subway.line.domain.Station;

public interface SubwayFarePolicy {

  Money calculate(final List<Line> lines, final Station departure, final Station arrival);
}
