package subway.domain.path;

import java.util.List;
import subway.domain.line.Line;
import subway.domain.station.Station;

public interface ShortestPathCalculator {

    Path findPath(final List<Line> lines, final Station sourceStation, final Station targetStation);
}
