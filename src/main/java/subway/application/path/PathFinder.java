package subway.application.path;


import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

public interface PathFinder {

    ShortestPath findShortestPath(List<Section> sections, Station upStationId, Station downStationId);
}
