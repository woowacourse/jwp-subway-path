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
            throw new IllegalStateException("이미 노선 내 구간이 존재합니다.");
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

    public Section peekByFirstStationUnique(final Station firstStation) {
        final LinkedList<Section> findSections = sections.stream()
                .filter(section -> section.matchFirstStationByName(firstStation))
                .collect(Collectors.toCollection(LinkedList::new));

        validateUniqueSection(findSections);
        return findSections.peekFirst();
    }

    public Section peekBySecondStationUnique(final Station secondStation) {
        final LinkedList<Section> findSections = sections.stream()
                .filter(section -> section.matchSecondStationName(secondStation))
                .collect(Collectors.toCollection(LinkedList::new));

        validateUniqueSection(findSections);
        return findSections.peekFirst();
    }

    private void validateUniqueSection(final List<Section> sections) {
        if (sections.size() != 1) {
            throw new IllegalStateException("유일한 구간을 찾을 수 없습니다.");
        }
    }

    private void validateDuplication(final Station station) {
        final boolean isExistStation = sections.stream()
                .anyMatch(section -> section.contains(station));

        if (isExistStation) {
            throw new IllegalStateException("이미 존재하는 등록된 역 입니다: " + station.getStationName());
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
