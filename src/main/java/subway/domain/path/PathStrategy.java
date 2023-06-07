package subway.domain.path;

import subway.domain.subway.Distance;
import subway.domain.subway.Section;

import java.util.List;
import java.util.Map;

public interface PathStrategy {
    Map.Entry<List<Long>, Distance> getPathAndDistance(List<Section> allSections, Long startStationId, Long targetStationId);
}
