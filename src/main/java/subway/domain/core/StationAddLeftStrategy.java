package subway.domain.core;

import static subway.domain.core.Direction.RIGHT;

import java.util.List;
import java.util.Optional;

public class StationAddLeftStrategy implements StationAddStrategy {
    @Override
    public void add(
            final List<Section> sections,
            final Station base,
            final Station additional,
            final Distance distance
    ) {
        final Optional<Section> section = findSectionByStationExistsAtDirection(sections, base, RIGHT);
        if (section.isPresent()) {
            final Section originSection = section.get();
            sections.add(new Section(originSection.getStart(), additional, originSection.subtract(distance)));
            sections.remove(originSection);
        }
        sections.add(new Section(additional, base, distance));
    }
}
