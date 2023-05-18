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

    private static final String INVALID_ALREADY_EXISTS_TWO_STATIONS_MESSAGE = "노선에 이미 존재하는 두 역을 등록할 수 없습니다.";
    private static final String INVALID_NOT_EXISTS_TWO_STATIONS_MESSAGE = "존재하지 않는 역들과의 구간을 등록할 수 없습니다.";
    private static final String INVALID_NOT_FOUND_RIGHT_SIDE_SECTION_MESSAGE = "오른쪽 구간을 찾을 수 없습니다.";
    private static final String INVALID_NOT_FOUND_LEFT_SIDE_SECTION_MESSAGE = "왼쪽 구간을 찾을 수 없습니다.";
    private static final String INVALID_DISTANCE_BETWEEN_BASE_AND_NEW_STATION_MESSAGE = "기존 역 사이 길이보다 크거나 같은 길이의 구간을 등록할 수 없습니다.";
    private static final String INVALID_NOT_FOUND_STATION_MESSAGE = "역이 노선에 없습니다.";

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

    private static Station findStartStation(final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        return stations.vertexSet()
                .stream()
                .filter(station -> isStartStation(stations, station))
                .findFirst()
                .orElse(null);
    }

    private static boolean isStartStation(final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations,
                                          final Station station) {
        return stations.incomingEdgesOf(station).isEmpty();
    }

    public Sections findUpdateSectionsByAddingSection(final Section section) {
        Station left = section.getLeft();
        Station right = section.getRight();
        int distance = section.getDistance();

        validateStation(left, right);
        return getSections(left, right, distance);
    }

    private void validateStation(final Station left, final Station right) {
        if (hasStation(left) && hasStation(right)) {
            throw new SubwayServiceException(INVALID_ALREADY_EXISTS_TWO_STATIONS_MESSAGE);
        }
        if (!hasStation(left) && !hasStation(right) && !isStationEmpty()) {
            throw new SubwayServiceException(INVALID_NOT_EXISTS_TWO_STATIONS_MESSAGE);
        }
    }

    private Sections getSections(final Station left, final Station right, final int distance) {
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

    private Sections getSectionsInBetween(final Station left, final Station right, final int distance, final Side findingSide) {
        Section newSection = new Section(left, right, new Distance(distance));
        Station baseStation = findBaseStationBySide(left, right, findingSide);
        if (hasSectionBySide(baseStation, findingSide)) {
            Section existedSection = findExistedSectionBySide(baseStation, findingSide);
            Section updatedSection = getUpdatedSection(existedSection, left, right, distance, findingSide);
            return new Sections(List.of(newSection, updatedSection));
        }
        return new Sections(List.of(newSection));
    }

    private Station findBaseStationBySide(final Station left, final Station right, final Side side) {
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

    private boolean hasRightSection(final Station station) {
        validateStation(station);
        return hasOutgoingStation(station);
    }

    private boolean hasOutgoingStation(final Station station) {
        return !stations.outgoingEdgesOf(station).isEmpty();
    }

    private boolean hasStation(final Station station) {
        return stations.containsVertex(station);
    }

    private boolean hasLeftSection(final Station station) {
        validateStation(station);
        return isStartStation(stations, station);
    }

    private Section findExistedSectionBySide(final Station station, final Side side) {
        if (side.isRight()) {
            return findRightSection(station);
        }
        return findLeftSection(station);
    }

    private Section findRightSection(final Station station) {
        Set<DefaultWeightedEdge> edges = stations.outgoingEdgesOf(station);
        return edges.stream()
                .map(edge -> new Section(station, stations.getEdgeTarget(edge),
                        new Distance((int) stations.getEdgeWeight(edge))))
                .findFirst()
                .orElseThrow(() -> new SubwayInternalServerException(INVALID_NOT_FOUND_RIGHT_SIDE_SECTION_MESSAGE));
    }

    private Section findLeftSection(final Station station) {
        Set<DefaultWeightedEdge> edges = stations.incomingEdgesOf(station);
        return edges.stream()
                .map(edge -> new Section(stations.getEdgeSource(edge), station,
                        new Distance((int) stations.getEdgeWeight(edge))))
                .findFirst()
                .orElseThrow(() -> new SubwayInternalServerException(INVALID_NOT_FOUND_LEFT_SIDE_SECTION_MESSAGE));
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
        if (existedDistance <= newDistance) {
            throw new SubwayServiceException(INVALID_DISTANCE_BETWEEN_BASE_AND_NEW_STATION_MESSAGE);
        }
    }

    public Sections findUpdateSectionsByDeletingSection(Station station) {
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
            throw new SubwayServiceException(INVALID_NOT_FOUND_STATION_MESSAGE);
        }
    }

    public List<Station> getOrderedStations() {
        List<Station> orderedStations = new ArrayList<>();
        if (isStationEmpty()) {
            return orderedStations;
        }
        Station station = new Station(start.getId(), start.getName());
        while (hasOutgoingStation(station)) {
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