package subway.section.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.station.domain.Station;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Sections {
    private static final int ADDITIONAL_SECTIONS_NUMBER_OF_BETWEEN_CASE = 3;
    private static final int NUMBER_OF_CONTAIN_MIDDLE_STATION = 2;
    
    private final Set<Section> sections;
    
    public Sections() {
        this(new HashSet<>());
    }
    
    public Sections(final Set<Section> sections) {
        this.sections = sections;
    }
    
    public void initAddStation(final String leftAdditional, final String rightAdditional, final long distance, final String lineName) {
        validateInitAddCase();
        sections.add(new Section(leftAdditional, rightAdditional, distance, lineName));
    }
    
    private void validateInitAddCase() {
        if (!sections.isEmpty()) {
            throw new IllegalArgumentException("해당 노선의 구간이 없을때만 최초 등록이 가능합니다.");
        }
    }
    
    public void addStation(
            final String base,
            final Direction direction,
            final String additionalStation,
            final long distance
    ) {
        validateExistStation(additionalStation);
        
        final Set<Section> additionalWithOneself =
                getAdditionalSectionsWithOneself(base, direction, additionalStation, distance);
        
        if (isBetweenCase(additionalWithOneself)) {
            removeIntersection(additionalWithOneself);
        }
        
        sections.addAll(additionalWithOneself);
    }
    
    private void validateExistStation(final String additionalStation) {
        if (isExistStation(additionalStation)) {
            throw new IllegalArgumentException(additionalStation + "은 이미 해당 노선에 존재하는 역입니다.");
        }
    }
    
    private boolean isExistStation(final String additionalStation) {
        return sections.stream()
                .anyMatch(section -> section.hasStation(additionalStation));
    }
    
    private Set<Section> getAdditionalSectionsWithOneself(
            final String base,
            final Direction direction,
            final String additionalStation,
            final long distance
    ) {
        return sections.stream()
                .filter(section -> section.hasStation(base))
                .map(section -> section.getAdditionalSectionsWithOneself(base, direction, additionalStation, distance))
                .max(Comparator.comparingInt(Set::size))
                .orElseThrow(() -> new IllegalArgumentException("기준역인 " + base + "이 해당 노선에 존재하지 않습니다."));
    }
    
    private boolean isBetweenCase(final Set<Section> additionalWithOneself) {
        return additionalWithOneself.size() >= ADDITIONAL_SECTIONS_NUMBER_OF_BETWEEN_CASE;
    }
    
    private void removeIntersection(final Set<Section> additionalWithOneself) {
        final Set<Section> sectionsOfRemoveIntersection = new HashSet<>(sections);
        sectionsOfRemoveIntersection.retainAll(additionalWithOneself);
        
        sections.removeAll(sectionsOfRemoveIntersection);
        additionalWithOneself.removeAll(sectionsOfRemoveIntersection);
    }
    
    public void removeStation(final String station) {
        final Set<Section> sectionsContainedStation = getSectionsOfContainStation(station);
        validateExistStation(sectionsContainedStation);
        sections.removeAll(sectionsContainedStation);
        
        if (isMiddleStationCase(sectionsContainedStation)) {
            sections.add(getCombinedSection(sectionsContainedStation));
        }
    }
    
    private Set<Section> getSectionsOfContainStation(final String station) {
        return sections.stream()
                .filter(section -> section.hasStation(station))
                .collect(Collectors.toUnmodifiableSet());
    }
    
    private void validateExistStation(final Set<Section> sectionsContainedStation) {
        if (sectionsContainedStation.isEmpty()) {
            throw new IllegalArgumentException("해당 노선에 삭제하려는 역이 존재하지 않습니다.");
        }
    }
    
    private boolean isMiddleStationCase(final Set<Section> sectionsOfContainStation) {
        return sectionsOfContainStation.size() >= NUMBER_OF_CONTAIN_MIDDLE_STATION;
    }
    
    private Section getCombinedSection(final Set<Section> sectionsContainedStation) {
        return sectionsContainedStation.stream()
                .reduce(Section::combine)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 역이 해당 노선에 존재하지 않습니다."));
    }
    
    public List<String> getSortedStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        
        final Set<Station> stations = new HashSet<>();
        sections.forEach(section -> section.putStationIfNotExist(stations));
        final Station frontStation = getStationOfMatchMostDirection(stations, Section::isLeftStation);
        final Station endStation = getStationOfMatchMostDirection(stations, Section::isRightStation);
        
        return sort(frontStation, endStation).stream()
                .map(Station::getName)
                .collect(Collectors.toUnmodifiableList());
    }
    
    private Station getStationOfMatchMostDirection(final Set<Station> stations, final BiPredicate<Section, Station> process) {
        return stations.stream()
                .filter(station -> isExistMatchStation(process, station))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("노선 안에 해당 역이 존재하지 않습니다."));
    }
    
    private boolean isExistMatchStation(final BiPredicate<Section, Station> process, final Station station) {
        return sections.stream()
                .anyMatch(section -> process.test(section, station));
    }
    
    private List<Station> sort(final Station frontStation, final Station endStation) {
        final WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        addSectionsToGraph(graph);
        final DijkstraShortestPath<Station, Section> path = new DijkstraShortestPath<>(graph);
        
        return path.getPath(frontStation, endStation).getVertexList();
    }
    
    public void addSectionsToGraph(final WeightedMultigraph<Station, Section> graph) {
        sections.forEach(section -> section.addStationsAndDistanceToGraph(graph));
    }
    
    public boolean isContainsStation(final String stationName) {
        return sections.stream()
                .anyMatch(section -> isExistMatchStation(Section::isLeftStation, new Station(stationName))
                || isExistMatchStation(Section::isRightStation, new Station(stationName)));
    }
    
    public Set<Section> getSections() {
        return sections;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
    
    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }
}
