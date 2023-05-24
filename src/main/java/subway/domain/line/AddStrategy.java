package subway.domain.line;

import java.util.List;
import java.util.Optional;
import subway.domain.vo.Distance;

public interface AddStrategy {
    void activate(List<Section> sections, Station upStation, Station downStation, Distance distance);

    default Optional<Section> findSectionByStationExistsAtDirection(
            final List<Section> sections,
            final Station station,
            final Direction direction
    ) {
        return sections.stream()
                .filter(section -> section.isStationExistsAtDirection(station, direction))
                .findFirst();
    }
}
