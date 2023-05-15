package subway.domain.section;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;

public class Sections {

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

        addSection(section);
    }

    public void addSection(final Section section) {
        this.sections.add(section);
    }

    public void insertBehindStation(
            final Station firstStation,
            final Station additionStation,
            final StationDistance stationDistance
    ) {
        validateDuplication(additionStation);
        final Section section = peekByFirstStationUnique(firstStation);
        final List<Section> sections = section.separateByInsertionStation(additionStation, stationDistance);

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

    public void removeSection(final Section section) {
        validateExistSection(section);
        sections.remove(section);
    }

    private void validateExistSection(final Section other) {
        final boolean isExistSection = sections.stream()
                .anyMatch(section -> section.equals(other));

        if (!isExistSection) {
            throw new IllegalStateException("존재 하지 않는 구간입니다.");
        }
    }

    public Station getFrontStation() {
        final Set<Station> sectionSet = sections.stream()
                .map(Section::getSecondStation)
                .collect(Collectors.toSet());

        return sections.stream()
                .map(Section::getFirstStation)
                .filter(Predicate.not(sectionSet::contains))
                .findFirst()
                .orElseThrow();
    }

    public Station getEndStation() {
        final Set<Station> sectionSet = sections.stream()
                .map(Section::getFirstStation)
                .collect(Collectors.toSet());

        return sections.stream()
                .map(Section::getSecondStation)
                .filter(Predicate.not(sectionSet::contains))
                .findFirst()
                .orElseThrow();
    }


    public List<Section> getSections() {
        return sections;
    }
}
