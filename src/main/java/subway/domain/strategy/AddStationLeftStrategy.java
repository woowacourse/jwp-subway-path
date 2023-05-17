package subway.domain.strategy;

import java.util.List;
import java.util.Optional;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

public class AddStationLeftStrategy implements AddStationStrategy {

    @Override
    public void addStation(
            final List<Section> sections,
            final Station base,
            final Station additional,
            final Distance distance
    ) {
        final Optional<Section> sectionAtEnd = findSectionAtEnd(sections, base);
        if (sectionAtEnd.isPresent()) {
            final Section originSection = sectionAtEnd.get();
            changeDistance(sections, originSection.getStart(), additional, originSection, distance);
        }
        sections.add(new Section(additional, base, distance));
    }

    private Optional<Section> findSectionAtEnd(final List<Section> sections, final Station base) {
        return sections.stream()
                .filter(section -> section.isEnd(base))
                .findFirst();
    }
}
