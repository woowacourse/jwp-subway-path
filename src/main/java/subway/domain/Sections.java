package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Sections {

    private static final int INITIAL_SIZE = 1;

    private final List<Section> sections;

    Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(List<Section> unsortedSections) {
        return SectionsFactory.createSortedSections(unsortedSections);
    }

    public boolean isBetweenStation(Station station) {
        return !inDownTerminal(station) && !isUpTerminal(station);
    }

    public boolean inDownTerminal(Station station) {
        return Objects.equals(sections.get(sections.size() - 1).getDownStation(), station);
    }

    public boolean isUpTerminal(Station station) {
        return Objects.equals(sections.get(0).getUpStation(), station);
    }

    public boolean isUpwardStation(Station station) {
        return sections.stream()
                .anyMatch(section -> Objects.equals(section.getUpStation(), station));
    }

    public boolean isDownwardStation(Station station) {
        return sections.stream()
                .anyMatch(section -> Objects.equals(section.getDownStation(), station));
    }

    public Section findUpwardStationSection(Station upStation) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getUpStation(), upStation))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("찾을 수 없는 구간입니다."));
    }

    public Section findDownwardStationSection(Station downStation) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getDownStation(), downStation))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("찾을 수 없는 구간입니다."));
    }

    public Sections findIncludeTargetSection(Station betweenStation) {
        final List<Section> sections = this.sections.stream()
                .filter(section -> Objects.equals(section.getUpStation(), betweenStation) || Objects.equals(section.getDownStation(), betweenStation))
                .collect(toList());

        return new Sections(sections);
    }

    public boolean isInitialState() {
        return sections.size() == INITIAL_SIZE;
    }

    public Long findFirstSectionId() {
        return sections.get(0).getId();
    }

    public Long findLastSectionId() {
        return sections.get(sections.size() - 1).getId();
    }

    public Distance calculateTotalDistance() {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(Distance.zero(), Distance::plus);
    }

    public List<Station> findAllStationsByOrder() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(toList());
    }

    public boolean canInsert(Station upStation, Station downStation) {
        final Set<Station> allStations = findAllStations();
        return allStations.contains(upStation) == allStations.contains(downStation);
    }

    private Set<Station> findAllStations() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(toSet());
    }

    public boolean hasSection(Station upStation, Station downStation) {
        return sections.stream()
                .anyMatch(section -> section.hasSameSection(upStation, downStation)
                        || section.hasSameSection(downStation, upStation)
                );
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
