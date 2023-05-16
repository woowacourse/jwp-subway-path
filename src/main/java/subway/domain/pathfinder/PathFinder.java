package subway.domain.pathfinder;

import java.util.List;

import subway.domain.Section;

public interface PathFinder {
    void addSections(List<Section> sections);

    List<Long> computeShortestPath(Long sourceStationId, Long targetStationId);

    Integer computeShortestDistance(Long sourceStationId, Long targetStationId);
}
