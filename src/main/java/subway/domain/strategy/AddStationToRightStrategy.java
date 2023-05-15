package subway.domain.strategy;

import static subway.domain.Direction.LEFT;

import java.util.List;
import java.util.Optional;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

public class AddStationToRightStrategy implements AddStationStrategy {
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
