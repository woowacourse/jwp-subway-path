package subway.domain;

import subway.exception.DuplicateStationInLineException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Line {

    private static final int NONE = 0;
    public static final Station EMPTY_ENDPOINT_STATION = new Station("종점역에 연결된 더미 데이터");

    private Long id;
    private final LineName name;
    private final LinkedList<Section> sections;

    public Line(LineName name, List<Section> sections) {
        validateEmptySection(sections);
        this.name = name;
        this.sections = new LinkedList<>(sections);
        addEmptyEndpoints(sections);
    }

    public Line(LineName name, Section section) {
        this(name, List.of(section));
    }

    public Line(Line otherLine) {
        this(otherLine.getId(), otherLine.getName(), otherLine.getSectionsWithoutEndPoints());
    }

    public Line(long lineId, LineName lineName, List<Section> sections) {
        this(lineName, sections);
        this.id = lineId;
    }

    private void validateEmptySection(List<Section> sections) {
        if (sections.size() == NONE) {
            throw new IllegalArgumentException("디버깅: section이 존재하지 않습니다.");
        }
    }

    private void addEmptyEndpoints(List<Section> sections) {
        Station upstreamEmptyEndpoint = sections.get(0).getUpstream();
        Station downstreamEmptyEndpoint = sections.get(sections.size() - 1).getDownstream();

        this.sections.addFirst(new Section(EMPTY_ENDPOINT_STATION, upstreamEmptyEndpoint, Integer.MAX_VALUE));
        this.sections.addLast(new Section(downstreamEmptyEndpoint, EMPTY_ENDPOINT_STATION, Integer.MAX_VALUE));
    }

    public List<Section> addStation(Station newStation, Station upstream, Station downstream, int distanceToUpstream) {
        validateDuplicateStations(newStation);
        Section sectionToAddNewStation = getSectionContaining(upstream, downstream);
        List<Section> sectionsToAdd = sectionToAddNewStation.insertInTheMiddle(newStation, distanceToUpstream);
        addSections(sectionToAddNewStation, sectionsToAdd);

        return sectionsToAdd;
    }

    private void validateDuplicateStations(Station newStation) {
        if (hasStation(newStation)) {
            throw new DuplicateStationInLineException("노선에 이미 존재하는 역입니다.");
        }
    }

    private Section getSectionContaining(Station upstream, Station downstream) {
        return sections.stream()
                .filter(section -> section.containsSameStations(upstream, downstream))
                .findAny()
                .orElseThrow(() -> new SectionNotFoundException("노선에 해당하는 구간이 존재하지 않습니다; upstream=" + upstream.toString() + " downstream=" + downstream.toString()));
    }

    private void addSections(Section section, List<Section> sectionsToAdd) {
        sections.add(sections.indexOf(section), sectionsToAdd.get(1));
        sections.add(sections.indexOf(sectionsToAdd.get(1)), sectionsToAdd.get(0));

        sections.remove(section);
    }

    public void deleteStation(Station stationToDelete) {
        validateStationExist(stationToDelete);
        List<Section> sectionsToMerge = findSectionsToMerge(stationToDelete);
        Section mergedSection = sectionsToMerge.get(0).merge(sectionsToMerge.get(1));

        deleteSections(sectionsToMerge, mergedSection);
    }

    private void validateStationExist(Station stationToDelete) {
        if (!hasStation(stationToDelete)) {
            throw new StationNotFoundException("노선에 존재하지 않는 역입니다.");
        }
    }

    private boolean hasStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    private List<Section> findSectionsToMerge(Station stationToDelete) {
        return sections.stream()
                .filter(section -> section.contains(stationToDelete))
                .collect(Collectors.toList());
    }

    private void deleteSections(List<Section> sectionsToMerge, Section mergedSection) {
        sections.add(sections.indexOf(sectionsToMerge.get(0)), mergedSection);

        sections.remove(sectionsToMerge.get(0));
        sections.remove(sectionsToMerge.get(1));
    }

    public List<String> getStationNamesInOrder() {
        return sections.subList(0, sections.size() - 1).stream()
                .map(section -> section.getDownstream().getName())
                .collect(Collectors.toList());
    }

    /**
     * Line 저장시 자동으로 추가되는 더미 Station을 제외한 나머지 Sections를 반환합니다.
     */
    public List<Section> getSectionsWithoutEndPoints() {
        return new LinkedList<>(sections.subList(1, sections.size() - 1));
    }

    public LineName getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sections);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name=" + name +
                ", sections=" + sections +
                '}';
    }
}
