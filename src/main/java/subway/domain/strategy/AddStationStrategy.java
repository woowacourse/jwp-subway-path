package subway.domain.strategy;

import java.util.List;
import java.util.Optional;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

public interface AddStationStrategy {

    void add(final List<Section> sections, final Station base, final Station additional, final Distance distance);

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
