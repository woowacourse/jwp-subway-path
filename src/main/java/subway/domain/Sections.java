package subway.domain;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class Sections {
    private final DefaultDirectedGraph<Station, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private final Line line;

    public Sections(final Line line) {
        this.line = line;
    }

    public void createNewLine(Station upLineStation, Station downLineStation, int distance) {
        validateDistance(distance);
        graph.addVertex(upLineStation);
        graph.addVertex(downLineStation);
        graph.setEdgeWeight(graph.addEdge(upLineStation, downLineStation), distance);
    }

    public boolean isSameLine(Line line) {
        return this.line.equals(line);
    }

    public int findOrderOf(final Station station) {
        return findAllStationsInOrder().indexOf(station);
    }

    public Station findUpEndStation() {
        return graph.vertexSet().stream()
                .filter(vertex -> this.graph.incomingEdgesOf(vertex).isEmpty())
                .findFirst()
                .orElse(null);
    }

    public Station findDownEndStation() {
        return graph.vertexSet().stream()
                .filter(vertex -> this.graph.outgoingEdgesOf(vertex).isEmpty())
                .findFirst()
                .orElse(null);
    }

    public List<Station> findAllStationsInOrder() {
        List<Station> allStationsInOrder = new ArrayList<>();
        Station startStation = findUpEndStation();
        while (startStation != null) {
            allStationsInOrder.add(startStation);
            Set<DefaultWeightedEdge> outgoingEdges = graph.outgoingEdgesOf(startStation);
            if (outgoingEdges.isEmpty()) {
                startStation = null;
            } else {
                startStation = graph.getEdgeTarget(outgoingEdges.iterator().next());
            }
        }
        return allStationsInOrder;
    }

    public Map<List<Station>, Integer> findAllSectionsInOrder() {
        final Map<List<Station>, Integer> sections = new LinkedHashMap<>();

        Station currentStation = findUpEndStation();

        while (currentStation != null) {
            Set<DefaultWeightedEdge> outgoingEdges = graph.outgoingEdgesOf(currentStation);
            if (outgoingEdges.isEmpty()) {
                currentStation = null;
            } else {
                final DefaultWeightedEdge edge = outgoingEdges.iterator().next();
                final Station nextStation = graph.getEdgeTarget(edge);
                final int distance = (int) graph.getEdgeWeight(edge);
                sections.put(List.of(currentStation, nextStation), distance);
                currentStation = graph.getEdgeTarget(edge);
            }
        }

        return sections;
    }

    public int findDistanceBetween(Station upLineStation, Station downLineStation) {
        final DefaultWeightedEdge edge = graph.getEdge(upLineStation, downLineStation);
        return (int) graph.getEdgeWeight(edge);
    }

    public Station addStation(Station upLineStation, Station downLineStation, int distance) {
        validateStations(upLineStation, downLineStation);
        validateDistance(distance);

        // 기존 역: upLineStation, 새로운 역: downLineStation
        if (graph.containsVertex(upLineStation)) {

            // upLineStation[하행종점] -> downLineStation (새 역) -> nothing!
            int inDegree = graph.inDegreeOf(upLineStation);
            int outDegree = graph.outDegreeOf(upLineStation);

            final boolean isDownLastStation = inDegree == 1 && outDegree == 0;

            if (isDownLastStation) {
                addStationToDownLine(upLineStation, downLineStation, distance);
                return downLineStation;
            }

            // upLineStation -> downLineStation (새 역) -> 기존 다음 역
            final Set<DefaultWeightedEdge> outgoingEdges = graph.outgoingEdgesOf(upLineStation);
            addStationToDownLine(upLineStation, downLineStation, findNextStation(outgoingEdges), distance);
            return downLineStation;
        }

        // 기존 역: downLineStation, 새로운 역: upLineStation
        if (graph.containsVertex(downLineStation)) {
            graph.addVertex(upLineStation);

            // nothing -> upLineStation (새 역) -> downLineStation
            int inDegree = graph.inDegreeOf(downLineStation);
            int outDegree = graph.outDegreeOf(downLineStation);

            final boolean isUpFirstStation = inDegree == 0 && outDegree == 1;

            if (isUpFirstStation) {
                addStationToUpLine(upLineStation, downLineStation, distance);
                return upLineStation;
            }
            // 기존 상행 역 -> upLineStation (새 역) -> downLineStation
            final Set<DefaultWeightedEdge> defaultWeightedEdges = graph.incomingEdgesOf(downLineStation);
            final Station previousStation = findPreviousStation(defaultWeightedEdges);
            addStationToUpLine(previousStation, upLineStation, downLineStation, distance);
            return upLineStation;
        }
        throw new IllegalArgumentException("부적절한 입력입니다.");
    }

    private static void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("역 사이 거리는 양의 정수로 입력해 주세요.");
        }
    }

    private void validateStations(final Station upLineStation, final Station downLineStation) {
        if (upLineStation.equals(downLineStation)) {
            throw new IllegalArgumentException("서로 다른 역을 입력해 주세요.");
        }

        if (!graph.containsVertex(upLineStation) && !graph.containsVertex(downLineStation)) {
            throw new IllegalArgumentException("모두 새로운 역입니다. 새로운 역과 기존 역을 입력해 주세요.");
        }

        if (graph.containsVertex(upLineStation) && graph.containsVertex(downLineStation)) {
            throw new IllegalArgumentException("모두 이미 존재하는 역입니다. 하나의 새로운 역을 입력해 주세요.");
        }
    }

    private Station findPreviousStation(final Set<DefaultWeightedEdge> defaultWeightedEdges) {
        if (!defaultWeightedEdges.isEmpty()) {
            final DefaultWeightedEdge closestEdge = defaultWeightedEdges.iterator().next();
            return graph.getEdgeSource(closestEdge);
        }
        throw new IllegalStateException("이전 역이 존재하지 않습니다.");
    }

    private Station findNextStation(final Set<DefaultWeightedEdge> outgoingEdges) {
        if (!outgoingEdges.isEmpty()) {
            final DefaultWeightedEdge closestEdge = outgoingEdges.iterator().next();
            return graph.getEdgeTarget(closestEdge);
        }
        throw new IllegalStateException("다음 역이 존재하지 않습니다.");
    }

    private void addStationToUpLine(final Station newStation, final Station downLineStation, final int distance) {
        graph.addVertex(newStation);
        graph.setEdgeWeight(graph.addEdge(newStation, downLineStation), distance);
    }

    private void addStationToDownLine(final Station upLineStation, final Station newStation, final int distance) {
        graph.addVertex(newStation);
        graph.setEdgeWeight(graph.addEdge(upLineStation, newStation), distance);
    }

    private void addStationToUpLine(final Station downLinePreviousStation, final Station newStation, final Station downLineStation, final int distance) {
        graph.addVertex(newStation); //
        // downLinePreviousStation ~ downLineStation distance <= distance => 예외 발생
        final DefaultWeightedEdge edge = graph.getEdge(downLinePreviousStation, downLineStation);
        final int distanceBetweenDownLinePreviousStationAndDownLineStation = (int) graph.getEdgeWeight(edge);
        if (distanceBetweenDownLinePreviousStationAndDownLineStation <= distance) {
            throw new IllegalArgumentException("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }

        final int distanceBetweenDownLinePreviousStationAndNewStation = distanceBetweenDownLinePreviousStationAndDownLineStation - distance;

        // downLinePreviousStation, downLineStation 끊어줌
        graph.removeEdge(downLinePreviousStation, downLineStation);

        // downLinePreviousStation, newStation 연결 => distance: distanceBetweenDownLinePreviousStationAndNewStation
        graph.setEdgeWeight(graph.addEdge(downLinePreviousStation, newStation), distanceBetweenDownLinePreviousStationAndNewStation);

        // newStation, downLineStation 연결 => distance: 입력한 거리
        graph.setEdgeWeight(graph.addEdge(newStation, downLineStation), distance);
    }

    private void addStationToDownLine(final Station upLineStation, final Station newStation, final Station upLineNextStation, final int distance) {
        graph.addVertex(newStation); //
        // upLineStation ~ upLineNextStation distance <= distance => 예외 발생
        DefaultWeightedEdge edge = graph.getEdge(upLineStation, upLineNextStation);
        int distanceBetweenUpLineStationAndUpLineNextStation = (int) graph.getEdgeWeight(edge);
        if (distanceBetweenUpLineStationAndUpLineNextStation <= distance) {
            throw new IllegalArgumentException("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }

        final int distanceBetweenNewStationAndUpLineNextStation = distanceBetweenUpLineStationAndUpLineNextStation - distance;

        // upLineStation, upLineNextStation 끊어줌
        graph.removeEdge(upLineStation, upLineNextStation);
        // upLineStation, newStation 연결 -> distance: 입력한 거리
        graph.setEdgeWeight(graph.addEdge(upLineStation, newStation), distance);
        // newStation, upLineNextStation 연결 -> distance: 계산한 거리
        graph.setEdgeWeight(graph.addEdge(newStation, upLineNextStation), distanceBetweenNewStationAndUpLineNextStation);
    }

    public void remove(Station station) {
        List<DefaultWeightedEdge> adjacentEdges = new ArrayList<>(graph.edgesOf(station));

        // 양쪽 연결된 경우
        if (adjacentEdges.size() == 2) {
            removeMiddleStation(station);
        }

        // 한 쪽만 연결된 경우
        if (adjacentEdges.size() == 1) {
            graph.removeEdge(adjacentEdges.get(0));
        }

        // Station 지우기
        graph.removeVertex(station);
    }

    private void removeMiddleStation(Station station) {
        Set<DefaultWeightedEdge> edgesToRemove = new HashSet<>();
        Station upLineStation = null;
        Station downLineStation = null;
        int distanceBetweenUpLineStationAndStation = 0;
        int distanceBetweenStationAndDownLineStation = 0;

        for (DefaultWeightedEdge edge : graph.incomingEdgesOf(station)) {
            edgesToRemove.add(edge);
            distanceBetweenUpLineStationAndStation = (int) graph.getEdgeWeight(edge);
            upLineStation = graph.getEdgeSource(edge);
        }

        for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(station)) {
            edgesToRemove.add(edge);
            distanceBetweenStationAndDownLineStation = (int) graph.getEdgeWeight(edge);
            downLineStation = graph.getEdgeTarget(edge);
        }

        // 거리 재배치
        int distance = distanceBetweenUpLineStationAndStation + distanceBetweenStationAndDownLineStation;

        // 연결 다시 이어주기
        graph.setEdgeWeight(graph.addEdge(upLineStation, downLineStation), distance);

        // 지우기
        graph.removeAllEdges(edgesToRemove);
        graph.removeVertex(station);
    }
}
