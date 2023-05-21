package subway.domain.core;

import java.util.List;
import java.util.Optional;

public interface StationAddStrategy {

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
