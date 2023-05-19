package subway.domain.pathfinder;

import java.util.List;

import subway.domain.PathInformation;
import subway.domain.Section;

public interface PathFinder {
    void addSections(List<Section> sections);

    PathInformation computeShortestPath(Long sourceStationId, Long targetStationId);
}
