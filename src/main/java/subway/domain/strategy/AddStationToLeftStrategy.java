package subway.domain.strategy;

import static subway.domain.Direction.RIGHT;

import java.util.List;
import java.util.Optional;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

public class AddStationToLeftStrategy implements AddStationStrategy {
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
