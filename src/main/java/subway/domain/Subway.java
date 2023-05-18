package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import subway.application.exception.SubwayInternalServerException;
import subway.application.exception.SubwayServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Subway {

    private static final String INVALID_ALREADY_EXISTS_TWO_STATIONS_MESSAGE = "노선에 이미 존재하는 두 역을 등록할 수 없습니다.";
    private static final String INVALID_NOT_EXISTS_TWO_STATIONS_MESSAGE = "존재하지 않는 역들과의 구간을 등록할 수 없습니다.";
    private static final String INVALID_NOT_FOUND_RIGHT_SIDE_SECTION_MESSAGE = "오른쪽 구간을 찾을 수 없습니다.";
    private static final String INVALID_NOT_FOUND_LEFT_SIDE_SECTION_MESSAGE = "왼쪽 구간을 찾을 수 없습니다.";
    private static final String INVALID_DISTANCE_BETWEEN_BASE_AND_NEW_STATION_MESSAGE = "기존 역 사이 길이보다 크거나 같은 길이의 구간을 등록할 수 없습니다.";

    private final Line line;
    private final SubwayStructure subwayStructure;
    private final Station start;

    private Subway(final Line line, final SubwayStructure subwayStructure,
                   final Station start) {
        this.line = line;
        this.subwayStructure = subwayStructure;
        this.start = start;
    }

    public static Subway of(final Line line, final List<Section> sections) {
        SubwayStructure subwayStructure = generateSubwayStructure(sections);
        Station start = subwayStructure.findStartStation();
        return new Subway(line, subwayStructure, start);
    }

    private static SubwayStructure generateSubwayStructure(final List<Section> sections) {
        SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> subwayStructure = new SimpleDirectedWeightedGraph<>(
                DefaultWeightedEdge.class);
        for (Section section : sections) {
            Station right = section.getRight();
            Station left = section.getLeft();

            subwayStructure.addVertex(right);
            subwayStructure.addVertex(left);
            subwayStructure.setEdgeWeight(subwayStructure.addEdge(left, right), section.getDistance());
        }
        return new SubwayStructure(subwayStructure);
    }

    public Sections findUpdateSectionsByAddingSection(final Section section) {
        Station left = section.getLeft();
        Station right = section.getRight();
        int distance = section.getDistance();

        validateStation(left, right);
        return getSections(left, right, distance);
    }

    private void validateStation(final Station left, final Station right) {
        if (subwayStructure.hasStation(left) && subwayStructure.hasStation(right)) {
            throw new SubwayServiceException(INVALID_ALREADY_EXISTS_TWO_STATIONS_MESSAGE);
        }
        if (subwayStructure.hasNoStation(left) && subwayStructure.hasNoStation(right) && subwayStructure.isStationsPresent()) {
            throw new SubwayServiceException(INVALID_NOT_EXISTS_TWO_STATIONS_MESSAGE);
        }
    }

    private Sections getSections(final Station left, final Station right, final int distance) {
        if (subwayStructure.hasStation(left) && subwayStructure.hasNoStation(right)) {
            return getLeftSectionsInBetween(left, right, distance);
        }
        if (subwayStructure.hasNoStation(left) && subwayStructure.hasStation(right)) {
            return getRightSectionsInBetween(left, right, distance);
        }
        return new Sections(List.of(new Section(left, right, new Distance(distance))));
    }

    private Sections getLeftSectionsInBetween(final Station left, final Station right, final int distance) {
        Section newSection = new Section(left, right, new Distance(distance));
        if (subwayStructure.hasRightSection(left)) {
            Section existedSection = findRightSection(left);
            Section updatedRightSection = getUpdatedRightSection(existedSection, right, distance);
            return new Sections(List.of(newSection, updatedRightSection));
        }
        return new Sections(List.of(newSection));
    }

    private Section findRightSection(final Station station) {
        Set<DefaultWeightedEdge> edges = subwayStructure.getRightEdge(station);
        return edges.stream()
                .map(edge -> new Section(station, subwayStructure.getRightStationByEdge(edge),
                        new Distance(subwayStructure.getDistance(edge))))
                .findFirst()
                .orElseThrow(() -> new SubwayInternalServerException(INVALID_NOT_FOUND_RIGHT_SIDE_SECTION_MESSAGE));
    }

    private Section getUpdatedRightSection(final Section existedSection, final Station newRightStation, final int newDistance) {
        int existedDistance = existedSection.getDistance();
        validateNewDistance(newDistance, existedDistance);

        return new Section(newRightStation, existedSection.getRight(), new Distance(existedDistance - newDistance));
    }

    private void validateNewDistance(int newDistance, int existedDistance) {
        if (existedDistance <= newDistance) {
            throw new SubwayServiceException(INVALID_DISTANCE_BETWEEN_BASE_AND_NEW_STATION_MESSAGE);
        }
    }

    private Sections getRightSectionsInBetween(Station left, Station right, int distance) {
        Section newSection = new Section(left, right, new Distance(distance));
        if (subwayStructure.hasLeftSection(right)) {
            Section existedSection = findLeftSection(right);
            Section updatedLeftSection = getUpdatedLeftSection(existedSection, left, distance);
            return new Sections(List.of(newSection, updatedLeftSection));
        }
        return new Sections(List.of(newSection));
    }

    private Section findLeftSection(final Station station) {
        Set<DefaultWeightedEdge> edges = subwayStructure.getLeftEdge(station);
        return edges.stream()
                .map(edge -> new Section(subwayStructure.getLeftStationByEdge(edge), station,
                        new Distance(subwayStructure.getDistance(edge))))
                .findFirst()
                .orElseThrow(() -> new SubwayInternalServerException(INVALID_NOT_FOUND_LEFT_SIDE_SECTION_MESSAGE));
    }

    private Section getUpdatedLeftSection(Section existedSection, Station newLeftStation, int newDistance) {
        int existedDistance = existedSection.getDistance();
        validateNewDistance(newDistance, existedDistance);

        return new Section(existedSection.getLeft(), newLeftStation, new Distance(existedDistance - newDistance));
    }

    public Sections findUpdateSectionsByDeletingSection(Station station) {
        if (subwayStructure.hasRightSection(station) && subwayStructure.hasLeftSection(station)) {
            Section leftSection = findLeftSection(station);
            Section rightSection = findRightSection(station);

            Station left = leftSection.getLeft();
            Station right = rightSection.getRight();
            Distance distance = new Distance(leftSection.getDistance() + rightSection.getDistance());
            return new Sections(List.of(new Section(left, right, distance)));
        }
        return new Sections(new ArrayList<>());
    }

    public List<Station> getOrderedStations() {
        List<Station> orderedStations = new ArrayList<>();
        if (subwayStructure.isStationsEmpty()) {
            return orderedStations;
        }
        Station station = new Station(start.getId(), start.getName());
        while (!subwayStructure.isEndStation(station)) {
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
