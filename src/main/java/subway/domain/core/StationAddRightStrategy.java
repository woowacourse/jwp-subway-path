package subway.domain.core;

import static subway.domain.core.Direction.LEFT;

import java.util.List;
import java.util.Optional;

public class StationAddRightStrategy implements StationAddStrategy {
    @Override
    public void add(
            final List<Section> sections,
            final Station base,
            final Station additional,
            final Distance distance
    ) {
        final Optional<Section> section = findSectionByStationExistsAtDirection(sections, base, LEFT);
        if (section.isPresent()) {
            final Section originSection = section.get();
            sections.add(new Section(additional, originSection.getEnd(), originSection.subtract(distance)));
            sections.remove(originSection);
        }
        sections.add(new Section(base, additional, distance));
    }
}
