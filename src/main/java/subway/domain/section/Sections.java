package subway.domain.section;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;

public class Sections {
    private final List<Section> sections;

    public Sections() {
        this.sections = new LinkedList<>();
    }

    public void addInitialStations(final Section section) {
        if (!sections.isEmpty()) {
            throw new IllegalStateException();
        }

        this.sections.add(section);
    }

    public void addFirstStation(
            final Station from,
            final Station station,
            final StationDistance stationDistance
    ) {
        final Section firstSection = peekUniqueSectionByFromStation(from);
        final Section attachedSection = firstSection.attachFront(station, stationDistance);

        sections.add(attachedSection);
    }

    public Section peekUniqueSectionByFromStation(final Station from) {
        final LinkedList<Section> findSections = sections.stream()
                .filter(section -> section.matchFromStation(from))
                .collect(Collectors.toCollection(LinkedList::new));

        validateUniqueSection(findSections);
        return findSections.peekFirst();
    }

    private void validateUniqueSection(final List<Section> collect) {
        if (collect.size() != 1) {
            throw new IllegalStateException();
        }
    }

    public List<Section> getSections() {
        return sections;
    }
}
