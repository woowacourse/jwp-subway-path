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

    public void attachAtFirstStation(
            final Station firstStation,
            final Station additionStation,
            final StationDistance stationDistance
    ) {
        final Section firstSection = peekByFirstStationUnique(firstStation);
        final Section attachedSection = firstSection.createFrontSection(additionStation, stationDistance);

        sections.add(attachedSection);
    }

    public void attachAtLastStation(
            final Station lastStation,
            final Station additionStation,
            final StationDistance stationDistance
    ) {
        final Section lastSection = peekBySecondStationUnique(lastStation);
        final Section attachedSection = lastSection.createBehindSection(additionStation, stationDistance);

        sections.add(attachedSection);
    }

    public void insertBehindStation(
            final Station firstStation,
            final Station additionStation,
            final StationDistance stationDistance
    ) {
        final Section section = peekByFirstStationUnique(firstStation);
        final List<Section> sections = section.separateByFirstStation(additionStation, stationDistance);

        this.sections.remove(section);
        this.sections.addAll(sections);
    }

    public Section peekByFirstStationUnique(final Station firstStation) { //FIXME 접근제어자
        final LinkedList<Section> findSections = sections.stream()
                .filter(section -> section.matchFirstStation(firstStation))
                .collect(Collectors.toCollection(LinkedList::new));

        validateUniqueSection(findSections);
        return findSections.peekFirst();
    }

    public Section peekBySecondStationUnique(final Station secondStation) { //FIXME 접근제어자
        final LinkedList<Section> findSections = sections.stream()
                .filter(section -> section.matchSecondStation(secondStation))
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
