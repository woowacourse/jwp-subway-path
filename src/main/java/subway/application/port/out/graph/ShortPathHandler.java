package subway.application.port.out.graph;

import subway.adapter.out.graph.dto.RouteDto;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.Map;

public interface ShortPathHandler {
    RouteDto findSortPath(final Station fromStation, final Station toStation, final Map<Long, Sections> sectionsByLine);
}
