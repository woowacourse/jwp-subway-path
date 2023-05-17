package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.exeption.InvalidDistanceException;
import subway.exeption.InvalidStationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

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

    public Station addStation(Station upLineStation, Station downLineStation, int distance) {
        validateStations(upLineStation, downLineStation);
        validateDistance(distance);

        if (isNewStation(upLineStation)) {
            final boolean isDownLastStation = graph.isDownLastStation(upLineStation);

            if (isDownLastStation) {
                addStationToDownLine(upLineStation, downLineStation, distance);
                return downLineStation;
            }

            final Set<DefaultWeightedEdge> outgoingEdges = getDownStationsOf(upLineStation);
            addStationToDownLine(upLineStation, downLineStation, findNextStation(outgoingEdges), distance);
            return downLineStation;
        }

        if (isNewStation(downLineStation)) {

            final boolean isUpFirstStation = graph.isUpFirstStation(downLineStation);

            if (isUpFirstStation) {
                addStationToUpLine(upLineStation, downLineStation, distance);
                return upLineStation;
            }

            final Set<DefaultWeightedEdge> defaultWeightedEdges = getUpStationsOf(downLineStation);
            final Station previousStation = findPreviousStation(defaultWeightedEdges);
            addStationToUpLine(previousStation, upLineStation, downLineStation, distance);
            return upLineStation;
        }

        throw new InvalidStationException("부적절한 입력입니다.");
    }

    private boolean isNewStation(final Station station) {
        return graph.containsStation(station);
    }

    public boolean isSameLine(Line line) {
        return this.line.equals(line);
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

    private Station findPreviousStation(final Set<DefaultWeightedEdge> defaultWeightedEdges) {
        if (!defaultWeightedEdges.isEmpty()) {
            final DefaultWeightedEdge closestEdge = defaultWeightedEdges.iterator().next();
            return graph.getUpStation(closestEdge);
        }
        throw new InvalidStationException("이전 역이 존재하지 않습니다.");
    }

    private Station findNextStation(final Set<DefaultWeightedEdge> outgoingEdges) {
        if (!outgoingEdges.isEmpty()) {
            final DefaultWeightedEdge closestEdge = outgoingEdges.iterator().next();
            return graph.getDownStation(closestEdge);
        }
        throw new InvalidStationException("다음 역이 존재하지 않습니다.");
    }

    private void addStationToUpLine(final Station newStation, final Station downLineStation, final int distance) {
        graph.addStation(newStation);
        graph.setSectionDistance(graph.addSection(newStation, downLineStation), distance);
    }

    private void addStationToDownLine(final Station upLineStation, final Station newStation, final int distance) {
        graph.addStation(newStation);
        graph.setSectionDistance(graph.addSection(upLineStation, newStation), distance);
    }

    private void addStationToUpLine(final Station downLinePreviousStation, final Station newStation, final Station downLineStation, final int distance) {
        graph.addStation(newStation); //
        // downLinePreviousStation ~ downLineStation distance <= distance => 예외 발생
        final DefaultWeightedEdge edge = graph.getSection(downLinePreviousStation, downLineStation);
        final int distanceBetweenDownLinePreviousStationAndDownLineStation = (int) graph.getSectionDistance(edge);
        if (distanceBetweenDownLinePreviousStationAndDownLineStation <= distance) {
            throw new InvalidDistanceException("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }

        final int distanceBetweenDownLinePreviousStationAndNewStation = distanceBetweenDownLinePreviousStationAndDownLineStation - distance;

        // downLinePreviousStation, downLineStation 끊어줌
        graph.removeSection(downLinePreviousStation, downLineStation);

        // downLinePreviousStation, newStation 연결 => distance: distanceBetweenDownLinePreviousStationAndNewStation
        graph.setSectionDistance(graph.addSection(downLinePreviousStation, newStation), distanceBetweenDownLinePreviousStationAndNewStation);

        // newStation, downLineStation 연결 => distance: 입력한 거리
        graph.setSectionDistance(graph.addSection(newStation, downLineStation), distance);
    }

    private void addStationToDownLine(final Station upLineStation, final Station newStation, final Station upLineNextStation, final int distance) {
        graph.addStation(newStation); //
        // upLineStation ~ upLineNextStation distance <= distance => 예외 발생
        DefaultWeightedEdge edge = graph.getSection(upLineStation, upLineNextStation);
        int distanceBetweenUpLineStationAndUpLineNextStation = (int) graph.getSectionDistance(edge);
        if (distanceBetweenUpLineStationAndUpLineNextStation <= distance) {
            throw new InvalidDistanceException("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }

        final int distanceBetweenNewStationAndUpLineNextStation = distanceBetweenUpLineStationAndUpLineNextStation - distance;

        // upLineStation, upLineNextStation 끊어줌
        graph.removeSection(upLineStation, upLineNextStation);
        // upLineStation, newStation 연결 -> distance: 입력한 거리
        graph.setSectionDistance(graph.addSection(upLineStation, newStation), distance);
        // newStation, upLineNextStation 연결 -> distance: 계산한 거리
        graph.setSectionDistance(graph.addSection(newStation, upLineNextStation), distanceBetweenNewStationAndUpLineNextStation);
    }

    public void deleteStation(Station station) {
        List<DefaultWeightedEdge> adjacentEdges = new ArrayList<>(graph.sectionsOf(station));

        // 양쪽 연결된 경우
        if (adjacentEdges.size() == 2) {
            deleteMiddleStation(station);
        }

        // 한 쪽만 연결된 경우
        if (adjacentEdges.size() == 1) {
            graph.removeSection(adjacentEdges.get(0));
        }

        // Station 지우기
        graph.removeStation(station);
    }

    private void deleteMiddleStation(Station station) {
        Set<DefaultWeightedEdge> edgesToRemove = new HashSet<>();
        Station upLineStation = null;
        Station downLineStation = null;
        int distanceBetweenUpLineStationAndStation = 0;
        int distanceBetweenStationAndDownLineStation = 0;

        for (DefaultWeightedEdge edge : getUpStationsOf(station)) {
            edgesToRemove.add(edge);
            distanceBetweenUpLineStationAndStation = (int) graph.getSectionDistance(edge);
            upLineStation = graph.getUpStation(edge);
        }

        for (DefaultWeightedEdge edge : getDownStationsOf(station)) {
            edgesToRemove.add(edge);
            distanceBetweenStationAndDownLineStation = (int) graph.getSectionDistance(edge);
            downLineStation = graph.getDownStation(edge);
        }

        // 거리 재배치
        int distance = distanceBetweenUpLineStationAndStation + distanceBetweenStationAndDownLineStation;

        // 연결 다시 이어주기
        graph.setSectionDistance(graph.addSection(upLineStation, downLineStation), distance);

        // 지우기
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
