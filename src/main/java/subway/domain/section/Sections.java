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
            final Station firstStation,
            final Station additionStation,
            final StationDistance stationDistance
    ) {
        final Section firstSection = peekUniqueSectionByFromStation(firstStation);
        final Section attachedSection = firstSection.attachFront(additionStation, stationDistance);

        sections.add(attachedSection);
    }

    public void addLastStation(
            final Station lastStation,
            final Station additionStation,
            final StationDistance stationDistance
    ) {
        final Section firstSection = peekUniqueSectionByToStation(lastStation);
        final Section attachedSection = firstSection.attachBehind(additionStation, stationDistance);

        sections.add(attachedSection);
    }

    public Section peekUniqueSectionByFromStation(final Station from) { //FIXME 접근제어자
        final LinkedList<Section> findSections = sections.stream()
                .filter(section -> section.matchFromStation(from))
                .collect(Collectors.toCollection(LinkedList::new));

        validateUniqueSection(findSections);
        return findSections.peekFirst();
    }

    public Section peekUniqueSectionByToStation(final Station to) { //FIXME 접근제어자
        final LinkedList<Section> findSections = sections.stream()
                .filter(section -> section.matchToStation(to))
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
