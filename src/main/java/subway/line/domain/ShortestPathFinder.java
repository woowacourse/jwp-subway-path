package subway.line.domain;

import java.util.List;
import subway.line.domain.Line;
import subway.line.dto.ShortestPathResponse;
import subway.station.domain.Station;

public interface ShortestPathFinder {

  ShortestPathResponse getShortestPathResponse(final List<Line> lines, final Station fromStation, final Station toStation);
}
