package subway.domain.section;

import subway.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class SingleLineSections {

    private final List<Section> sections;

    SingleLineSections(List<Section> sections) {
        this.sections = sections;
    }

    public static SingleLineSections from(List<Section> unsortedSections) {
        return SectionsFactory.createSortedSections(unsortedSections);
    }

    public boolean isDownTerminal(Station station) {
        return Objects.equals(sections.get(sections.size() - 1).getDownStation(), station);
    }

    public boolean isUpTerminal(Station station) {
        return Objects.equals(sections.get(0).getUpStation(), station);
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

    public Long findFirstSectionId() {
        return sections.get(0).getId();
    }

    public Long findLastSectionId() {
        return sections.get(sections.size() - 1).getId();
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

    public Section findIncludeSectionByForwardStation(Station targetStation) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getUpStation(), targetStation))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("해당하는 구간을 찾을 수 없습니다."));
    }

    public Section findIncludeSectionByBackwardStation(Station targetStation) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getDownStation(), targetStation))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("해당하는 구간을 찾을 수 없습니다."));
    }
}
