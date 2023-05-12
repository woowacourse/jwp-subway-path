package subway.section.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import subway.station.domain.Station;
import subway.station.domain.StationDistance;

public class Sections {
    private static final int END_POINT_STATION_FLAG = 1;
    private static final int BETWEEN_STATION_FLAG = 2;

    private final List<Section> sections;

    public Sections() {
        this.sections = new LinkedList<>();
    }

    public Sections(final List<Section> sections) {
        this.sections = sections;
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
        validateDuplication(additionStation);
        final Section firstSection = peekByFirstStationUnique(firstStation);
        final Section attachedSection = firstSection.createFrontSection(additionStation, stationDistance);

        sections.add(attachedSection);
    }

    public void attachAtLastStation(
            final Station lastStation,
            final Station additionStation,
            final StationDistance stationDistance
    ) {
        validateDuplication(additionStation);
        final Section lastSection = peekBySecondStationUnique(lastStation);
        final Section attachedSection = lastSection.createBehindSection(additionStation, stationDistance);

        sections.add(attachedSection);
    }

    public void insertBehindStation(
            final Station firstStation,
            final Station additionStation,
            final StationDistance stationDistance
    ) {
        validateDuplication(additionStation);
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

    private void validateDuplication(final Station station) {
        final boolean isExistStation = sections.stream()
                .anyMatch(section -> section.contains(station));

        if (isExistStation) {
            throw new IllegalStateException();
        }
    }

    public void removeFirstStation(final Station station) {
        validateEndPointStation(station);
        final Section firstSection = peekByFirstStationUnique(station);
        sections.remove(firstSection);
    }

    public void removeLastStation(final Station station) {
        validateEndPointStation(station);
        final Section lastSection = peekBySecondStationUnique(station);
        sections.remove(lastSection);
    }

    public void removeStation(final Station station) {
        validateBetweenStation(station);
        final Section frontSection = peekBySecondStationUnique(station);
        final Section behindSection = peekByFirstStationUnique(station);

        final Section mergedSection = frontSection.merge(behindSection);

        sections.removeAll(List.of(frontSection, behindSection));
        sections.add(mergedSection);
    }

    private void validateEndPointStation(final Station station) {
        if (countStationInSection(station) != END_POINT_STATION_FLAG) {
            throw new IllegalArgumentException();
        }
    }

    private void validateBetweenStation(final Station station) {
        if (countStationInSection(station) != BETWEEN_STATION_FLAG) {
            throw new IllegalArgumentException();
        }
    }

    private int countStationInSection(final Station station) {
        return (int) sections.stream()
                .filter(section -> section.contains(station))
                .count();
    }

    public List<Section> getSections() {
        return sections;
    }
}
