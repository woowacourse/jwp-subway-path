package subway.domain.path.strategy;

import java.util.List;

import subway.domain.path.PathInfo;
import subway.domain.subway.Section;

public interface PathFindStrategy {
    PathInfo findPathInfo(
        Long departureId,
        Long destinationId,
        List<Section> allSections);
}
