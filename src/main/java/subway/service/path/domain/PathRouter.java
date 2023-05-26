package subway.service.path.domain;

import subway.service.path.dto.ShortestPath;
import subway.service.section.domain.Section;
import subway.service.station.domain.Station;

import java.util.List;

public interface PathRouter {

    ShortestPath findShortestPath(List<Section> sections, Station source, Station target);
}
