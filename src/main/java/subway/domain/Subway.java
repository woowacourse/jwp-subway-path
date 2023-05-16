package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import subway.application.exception.SubwayInternalServerException;
import subway.application.exception.SubwayServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static subway.domain.Side.LEFT;
import static subway.domain.Side.RIGHT;

public class Subway {

    private final Line line;
    private final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations;
    private final Station start;

    private Subway(final Line line, final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations,
                   final Station start) {
        this.line = line;
        this.stations = stations;
        this.start = start;
    }

    public static Subway of(final Line line, final List<Section> sections) {
        SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations = generateStations(sections);
        Station start = findStartStation(stations);
        return new Subway(line, stations, start);
    }

    private static SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> generateStations(final List<Section> sections) {
        SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations = new SimpleDirectedWeightedGraph<>(
                DefaultWeightedEdge.class);
        for (Section section : sections) {
            Station right = section.getRight();
            Station left = section.getLeft();

            stations.addVertex(right);
            stations.addVertex(left);
            stations.setEdgeWeight(stations.addEdge(left, right), section.getDistance());
        }
        return stations;
    }

    private static Station findStartStation(SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        Station station = getBaseStationForFindingStart(stations);
        while (station != null && !stations.incomingEdgesOf(station).isEmpty()) {
            station = stations.incomingEdgesOf(station)
                    .stream()
                    .map(stations::getEdgeSource)
                    .findFirst()
                    .orElse(null);
        }
        return station;
    }

    private static Station getBaseStationForFindingStart(SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        return stations.edgeSet()
                .stream()
                .map(stations::getEdgeSource)
                .findFirst()
                .orElse(null);
    }

    public Sections findAddSections(Section section) {
        Station left = section.getLeft();
        Station right = section.getRight();
        int distance = section.getDistance();

        validateStation(left, right);
        return getSections(left, right, distance);
    }

    private void validateStation(Station left, Station right) {
        if (hasStation(left) && hasStation(right)) {
            throw new SubwayServiceException("노선에 이미 존재하는 두 역을 등록할 수 없습니다.");
        }
        if (!hasStation(left) && !hasStation(right) && !isStationEmpty()) {
            throw new SubwayServiceException("존재하지 않는 역들과의 구간을 등록할 수 없습니다.");
        }
    }

    private Sections getSections(Station left, Station right, int distance) {
        if (hasStation(left) && !hasStation(right)) {
            return getSectionsInBetween(left, right, distance, RIGHT);
        }
        if (!hasStation(left) && hasStation(right)) {
            return getSectionsInBetween(left, right, distance, LEFT);
        }
        return new Sections(List.of(new Section(left, right, new Distance(distance))));
    }

    private boolean isStationEmpty() {
        return stations.edgeSet().isEmpty();
    }

    private Sections getSectionsInBetween(Station left, Station right, int distance, Side findingSide) {
        Section newSection = new Section(left, right, new Distance(distance));
        Station baseStation = findBaseStationBySide(left, right, findingSide);
        if (hasSectionBySide(baseStation, findingSide)) {
            Section existedSection = findExistedSectionBySide(baseStation, findingSide);
            Section updatedSection = getUpdatedSection(existedSection, left, right, distance, findingSide);
            return new Sections(List.of(newSection, updatedSection));
        }
        return new Sections(List.of(newSection));
    }

    private Station findBaseStationBySide(Station left, Station right, Side side) {
        if (side.isRight()) {
            return left;
        }
        return right;
    }

    private boolean hasSectionBySide(final Station station, final Side side) {
        if (side.isRight()) {
            return hasRightSection(station);
        }
        return hasLeftSection(station);
    }

    public boolean hasRightSection(final Station station) {
        validateStation(station);
        return !stations.outgoingEdgesOf(station).isEmpty();
    }

    public boolean hasStation(final Station station) {
        return stations.containsVertex(station);
    }

    public boolean hasLeftSection(final Station station) {
        validateStation(station);
        return !stations.incomingEdgesOf(station).isEmpty();
    }

    private Section findExistedSectionBySide(final Station station, final Side side) {
        if (side.isRight()) {
            return findRightSection(station);
        }
        return findLeftSection(station);
    }

    public Section findRightSection(final Station station) {
        Set<DefaultWeightedEdge> edge = stations.outgoingEdgesOf(station);
        return edge.stream()
                .map(x -> new Section(station, stations.getEdgeTarget(x),
                        new Distance((int) stations.getEdgeWeight(x))))
                .findFirst()
                .orElseThrow(() -> new SubwayInternalServerException("오른쪽 구간을 찾을 수 없습니다."));
    }

    public Section findLeftSection(final Station station) {
        Set<DefaultWeightedEdge> edge = stations.incomingEdgesOf(station);
        return edge.stream()
                .map(x -> new Section(stations.getEdgeSource(x), station,
                        new Distance((int) stations.getEdgeWeight(x))))
                .findFirst()
                .orElseThrow(() -> new SubwayInternalServerException("왼쪽 구간을 찾을 수 없습니다."));
    }

    private Section getUpdatedSection(Section baseSection, Station left, Station right, int newDistance, Side side) {
        int existedDistance = baseSection.getDistance();
        validateDistance(newDistance, existedDistance);

        if (side.isRight()) {
            return new Section(right, baseSection.getRight(), new Distance(existedDistance - newDistance));
        }
        return new Section(baseSection.getLeft(), left, new Distance(existedDistance - newDistance));
    }

    private static void validateDistance(int newDistance, int existedDistance) {
        if (existedDistance - newDistance <= 0) {
            throw new SubwayServiceException("기존 역 사이 길이보다 크거나 같은 길이의 구간을 등록할 수 없습니다.");
        }
    }

    public Sections findDeleteSections(Station station) {
        validateStation(station);
        if (hasRightSection(station) && hasLeftSection(station)) {
            Section leftSection = findLeftSection(station);
            Section rightSection = findRightSection(station);

            Station left = leftSection.getLeft();
            Station right = rightSection.getRight();
            Distance distance = new Distance(leftSection.getDistance() + rightSection.getDistance());
            return new Sections(List.of(new Section(left, right, distance)));
        }
        return new Sections(new ArrayList<>());
    }

    private void validateStation(Station station) {
        if (!hasStation(station)) {
            throw new SubwayServiceException("역이 노선에 없습니다.");
        }
    }

    public List<Station> getOrderedStations() {
        List<Station> orderedStations = new ArrayList<>();
        if (isStationEmpty()) {
            return orderedStations;
        }
        Station station = new Station(start.getId(), start.getName());
        while (!stations.outgoingEdgesOf(station).isEmpty()) {
            orderedStations.add(station);
            station = findRightSection(station).getRight();
        }
        orderedStations.add(station);
        return orderedStations;
    }

    public Line getLine() {
        return line;
    }
}
