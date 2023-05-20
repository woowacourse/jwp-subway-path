package subway.domain.path;

import java.util.List;
import java.util.Map;

import subway.domain.subway.Section;

public interface PathFindStrategy {
    Map.Entry<List<Long>, Integer> findPathAndTotalDistance(
        Long departureId,
        Long destinationId,
        List<Section> allSections);
}
