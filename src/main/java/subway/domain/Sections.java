package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.exeption.InvalidDistanceException;
import subway.exeption.InvalidStationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static subway.domain.Direction.DOWN;
import static subway.domain.Direction.UP;

public class Sections {
    private final Graph graph;
    private final Line line;

    public Sections(final Line line, final Graph graph) {
        this.graph = graph;
        this.line = line;
    }

    public void createInitialSection(Station upStation, Station downStation, int distance) {
        validateDistance(distance);
        graph.addStation(upStation);
        graph.addStation(downStation);
        graph.addSection(upStation, downStation);
        graph.setSectionDistance(graph.getSection(upStation, downStation), distance);
    }

    public Station addStation(Station upStation, Station downStation, int distance) {
        validateStations(upStation, downStation);
        validateDistance(distance);

        if (isNewStation(downStation)) {

            Station newStation = downStation;
            Station existingStation = upStation;

            if (graph.isTerminal(DOWN, existingStation)) {
                addStationTo(DOWN, existingStation, newStation, distance);
                return newStation;
            }

            final Set<DefaultWeightedEdge> adjacentSections = getAdjacentStationsOf(DOWN, existingStation);
            addStationToDownLine(existingStation, newStation, findAdjacentStation(DOWN, adjacentSections), distance);
            return newStation;
        }

        if (isNewStation(upStation)) {

            Station newStation = upStation;
            Station existingStation = downStation;

            if (graph.isTerminal(UP, existingStation)) {
                graph.addStation(newStation);
                graph.setSectionDistance(graph.addSection(newStation, existingStation), distance);
                return newStation;
            }

            final Set<DefaultWeightedEdge> adjacentSections = getAdjacentStationsOf(UP, existingStation);
            addStationToUpLine(findAdjacentStation(UP, adjacentSections), newStation, existingStation, distance);
            return newStation;
        }

        throw new InvalidStationException("부적절한 입력입니다.");
    }

    private void addStationTo(final Direction direction, final Station existingStation, final Station newStation, final int distance) {
        if (direction == DOWN) {
            graph.addStation(newStation);
            graph.setSectionDistance(graph.addSection(existingStation, newStation), distance);
        }
        if (direction == UP) {
            graph.addStation(newStation);
            graph.setSectionDistance(graph.addSection(newStation, existingStation), distance);
        }
    }

    private boolean isNewStation(final Station station) {
        return !graph.containsStation(station);
    }

    public boolean isSameLine(Line line) {
        return this.line.getId().equals(line.getId());
    }

    public Station findUpEndStation() {
        return graph.stationSet().stream()
                .filter(vertex -> this.graph.upStationsOf(vertex).isEmpty())
                .findFirst()
                .orElse(null);
    }

    public List<Station> findAllStationsInOrder() {
        List<Station> allStationsInOrder = new ArrayList<>();
        Station startStation = findUpEndStation();
        while (startStation != null) {
            allStationsInOrder.add(startStation);
            Set<DefaultWeightedEdge> outgoingEdges = getDownStationsOf(startStation);
            if (outgoingEdges.isEmpty()) {
                startStation = null;
            } else {
                startStation = graph.getDownStation(outgoingEdges.iterator().next());
            }
        }
        return allStationsInOrder;
    }

    public int findDistanceBetween(Station upLineStation, Station downLineStation) {
        final DefaultWeightedEdge edge = graph.getSection(upLineStation, downLineStation);
        return (int) graph.getSectionDistance(edge);
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new InvalidDistanceException("역 사이 거리는 양의 정수로 입력해 주세요.");
        }
    }

    private void validateStations(final Station upLineStation, final Station downLineStation) {
        if (upLineStation.equals(downLineStation)) {
            throw new InvalidStationException("서로 다른 역을 입력해 주세요.");
        }
    }

    private Station findAdjacentStation(final Direction direction, final Set<DefaultWeightedEdge> defaultWeightedEdges) {
        if (defaultWeightedEdges.isEmpty()) {
            throw new InvalidStationException("이전 역 혹은 다음 역이 존재하지 않습니다.");
        }
        if (direction == UP) {
            return graph.getUpStation(defaultWeightedEdges.iterator().next());
        }
        return graph.getDownStation(defaultWeightedEdges.iterator().next());
    }

    private void addStationToUpLine(final Station downLinePreviousStation, final Station newStation, final Station downLineStation, final int distance) {
        graph.addStation(newStation);

        final DefaultWeightedEdge edge = graph.getSection(downLinePreviousStation, downLineStation);
        final int distanceBetweenDownLinePreviousStationAndDownLineStation = (int) graph.getSectionDistance(edge);
        if (distanceBetweenDownLinePreviousStationAndDownLineStation <= distance) {
            throw new InvalidDistanceException("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }

        final int distanceBetweenDownLinePreviousStationAndNewStation = distanceBetweenDownLinePreviousStationAndDownLineStation - distance;

        graph.removeSection(downLinePreviousStation, downLineStation);
        graph.setSectionDistance(graph.addSection(downLinePreviousStation, newStation), distanceBetweenDownLinePreviousStationAndNewStation);
        graph.setSectionDistance(graph.addSection(newStation, downLineStation), distance);
    }

    private void addStationToDownLine(final Station upLineStation, final Station newStation, final Station upLineNextStation, final int distance) {
        graph.addStation(newStation);

        DefaultWeightedEdge edge = graph.getSection(upLineStation, upLineNextStation);
        int distanceBetweenUpLineStationAndUpLineNextStation = (int) graph.getSectionDistance(edge);
        if (distanceBetweenUpLineStationAndUpLineNextStation <= distance) {
            throw new InvalidDistanceException("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }

        final int distanceBetweenNewStationAndUpLineNextStation = distanceBetweenUpLineStationAndUpLineNextStation - distance;

        graph.removeSection(upLineStation, upLineNextStation);
        graph.setSectionDistance(graph.addSection(upLineStation, newStation), distance);
        graph.setSectionDistance(graph.addSection(newStation, upLineNextStation), distanceBetweenNewStationAndUpLineNextStation);
    }

    public void deleteStation(Station station) {
        List<DefaultWeightedEdge> adjacentEdges = new ArrayList<>(graph.sectionsOf(station));

        if (adjacentEdges.size() == 2) {
            deleteMiddleStation(station);
        }

        if (adjacentEdges.size() == 1) {
            graph.removeSection(adjacentEdges.get(0));
        }

        graph.removeStation(station);
    }

    private void deleteMiddleStation(Station station) {
        Set<DefaultWeightedEdge> edgesToRemove = new HashSet<>();
        Station upLineStation = null;
        Station downLineStation = null;
        int distanceBetweenUpLineStationAndStation = 0;
        int distanceBetweenStationAndDownLineStation = 0;

        for (DefaultWeightedEdge edge : getAdjacentStationsOf(UP, station)) {
            edgesToRemove.add(edge);
            distanceBetweenUpLineStationAndStation = (int) graph.getSectionDistance(edge);
            upLineStation = graph.getUpStation(edge);
        }

        for (DefaultWeightedEdge edge : getAdjacentStationsOf(DOWN, station)) {
            edgesToRemove.add(edge);
            distanceBetweenStationAndDownLineStation = (int) graph.getSectionDistance(edge);
            downLineStation = graph.getDownStation(edge);
        }

        int distance = distanceBetweenUpLineStationAndStation + distanceBetweenStationAndDownLineStation;

        graph.setSectionDistance(graph.addSection(upLineStation, downLineStation), distance);
        graph.removeAllSections(edgesToRemove);
        graph.removeStation(station);
    }

    public boolean containsStation(final Station station) {
        return graph.stationSet().contains(station);
    }

    public Station findAdjacentStationOf(
            final Station station,
            final Function<Station, Set<DefaultWeightedEdge>> method) {
        Station nextStation = null;
        DefaultWeightedEdge nextEdge = null;
        Set<DefaultWeightedEdge> edges = method.apply(station);
        if (!edges.isEmpty()) {
            nextEdge = edges.iterator().next();
            nextStation = graph.getDownStation(nextEdge);
        }
        return nextStation;
    }

    public Set<DefaultWeightedEdge> getAdjacentStationsOf(final Direction direction, final Station station) {
        if (direction == UP) {
            return graph.upStationsOf(station);
        }
        return graph.downStationsOf(station);
    }

    public Set<DefaultWeightedEdge> getUpStationsOf(final Station station) {
        return graph.upStationsOf(station);
    }

    public Set<DefaultWeightedEdge> getDownStationsOf(final Station station) {
        return graph.downStationsOf(station);
    }

    public Line getLine() {
        return line;
    }
}
