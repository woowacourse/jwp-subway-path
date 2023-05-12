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

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(List<Section> unsortedSections) {
        return new Sections(sortSections(unsortedSections));
    }

    private static List<Section> sortSections(List<Section> sections) {
        final Station firstStation = getFirstStations(sections);
        List<Section> result = new ArrayList<>();
        Station next = firstStation;
        for (int i = 0; i < sections.size(); i++) {
            next = addNextSection(sections, result, next);
        }
        return result;
    }

    private static Station getFirstStations(List<Section> sections) {
        final Set<Station> allStationIds = sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(toSet());

        final Set<Station> downStationIds = sections.stream()
                .map(Section::getDownStation)
                .collect(toSet());

        final List<Station> firstStation = new ArrayList<>(allStationIds);
        firstStation.removeAll(downStationIds);

        return firstStation.get(0);
    }

    private static Station addNextSection(List<Section> sections, List<Section> result, Station nextStation) {
        for (Section section : sections) {
            if (section.getUpStation().equals(nextStation)) {
                nextStation = section.getDownStation();
                result.add(section);
                break;
            }
        }
        return nextStation;
    }

    public boolean isDownEndPoint(Station upStation) {
        return Objects.equals(sections.get(sections.size() - 1).getDownStation(), upStation);
    }

    public boolean isUpEndPoint(Station upStation) {
        return Objects.equals(sections.get(0).getUpStation(), upStation);
    }

    public boolean isUpStationPoint(Station upStation) {
        return sections.stream()
                .anyMatch(section -> Objects.equals(section.getUpStation(), upStation));
    }

    public Section getTargtUpStationSection(Station upStation) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getUpStation(), upStation))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("찾을 수 없는 구간입니다."));
    }

    public Section getTargtDownStationSection(Station downStation) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getDownStation(), downStation))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("찾을 수 없는 구간입니다."));
    }

    public Sections findIncludeTargetSection(Station station) {
        final List<Section> sections = this.sections.stream()
                .filter(section -> Objects.equals(section.getUpStation(), station) || Objects.equals(section.getDownStation(), station))
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

    public boolean canInsert(Station upStation, Station downStation) {
        final Set<Station> allStations = findAllStations();
        return allStations.contains(upStation) == allStations.contains(downStation);
    }

    private Set<Station> findAllStations() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(toSet());
    }

    public List<Section> getSections() {
        return sections;
    }
}
