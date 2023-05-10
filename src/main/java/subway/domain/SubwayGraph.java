package subway.domain;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Set;

public class SubwayGraph {
    private final DefaultDirectedWeightedGraph<Station, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private final Line line;

    public SubwayGraph(final Line line) {
        this.line = line;
    }

    public void createNewLine(Station upLineStation, Station downLineStation, int distance) {
        graph.addVertex(upLineStation);
        graph.addVertex(downLineStation);
        graph.setEdgeWeight(graph.addEdge(upLineStation, downLineStation), distance);
    }

    public void addStation(Station upLineStation, Station downLineStation, int distance) {
        // TODO: upLineStation, downLineStation 둘 다 없으면 예외 처리

        // 기존 역: upLineStation, 새로운 역: downLineStation
        if (graph.containsVertex(upLineStation)) {

            // upLineStation[하행종점] -> downLineStation (새 역) -> nothing!
            int inDegree = graph.inDegreeOf(upLineStation);
            int outDegree = graph.outDegreeOf(upLineStation);

            final boolean isDownLastStation = inDegree == 1 && outDegree == 0;

            if (isDownLastStation) {
                addStationToDownLine(upLineStation, downLineStation, distance);
                return;
            }

            // upLineStation -> downLineStation (새 역) -> 기존 다음 역
            final Set<DefaultWeightedEdge> outgoingEdges = graph.outgoingEdgesOf(upLineStation);
            addStationToDownLine(upLineStation, downLineStation, findNextStation(outgoingEdges), distance);
            return;
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
                return;
            }
            // 기존 상행 역 -> upLineStation (새 역) -> downLineStation
            final Set<DefaultWeightedEdge> defaultWeightedEdges = graph.incomingEdgesOf(downLineStation);
            final Station previousStation = findPreviousStation(defaultWeightedEdges);
            addStationToUpLine(previousStation, upLineStation, downLineStation, distance);
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
}
