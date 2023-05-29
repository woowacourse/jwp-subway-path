package subway.domain.strategy;

import java.util.List;
import java.util.Optional;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

public class AddStationRightStrategy implements AddStationStrategy {

    @Override
    public void addStation(
            final List<Section> sections,
            final Station base,
            final Station additional,
            final Distance distance
    ) {
        final Optional<Section> sectionAtStart = findSectionAtStart(sections, base);
        if (sectionAtStart.isPresent()) {
            final Section originSection = sectionAtStart.get();
            changeDistance(sections, additional, originSection.getEnd(), originSection, distance);
        }
        sections.add(new Section(base, additional, distance));
    }

    private Optional<Section> findSectionAtStart(final List<Section> sections, final Station base) {
        return sections.stream()
                .filter(section -> section.isStart(base))
                .findFirst();
    }
}
