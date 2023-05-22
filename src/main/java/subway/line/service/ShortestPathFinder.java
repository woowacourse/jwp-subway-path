package subway.line.service;

import java.util.List;
import subway.line.domain.Line;
import subway.line.dto.ShortestPathResponse;
import subway.station.domain.Station;

public interface ShortestPathFinder {

  void makeGraph(final List<Line> lines);

  ShortestPathResponse getShortestPathResponse(final Station fromstation, final Station toStation);
}
