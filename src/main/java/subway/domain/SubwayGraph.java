package subway.domain;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.entity.EdgeEntity;
import subway.exception.StationAlreadyExistException;

import java.util.*;

public class SubwayGraph {
    private final DefaultDirectedWeightedGraph<Station, DefaultWeightedEdge> graph;
    private final Line line;

    public SubwayGraph(DefaultDirectedWeightedGraph<Station, DefaultWeightedEdge> graph, Line line) {
        this.graph = graph;
        this.line = line;
    }

    public SubwayGraph(final Line line) {
        this(new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class), line);

    }

    private void createInitStations(Station upLineEndStation, Station downLineEndStation, int distance) {
        validateDistance(distance);
        graph.addVertex(upLineEndStation);
        graph.addVertex(downLineEndStation);
        linkStation(upLineEndStation, downLineEndStation, distance);
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("역 사이 거리는 양의 정수로 입력해 주세요.");
        }
    }

    public List<Station> addStation(Station upLineStation, Station downLineStation, int distance) {
        validateStations(upLineStation, downLineStation);
        validateDistance(distance);

        if (getStationSize() == 0) {
            createInitStations(upLineStation, downLineStation, distance);
            return List.of(upLineStation, downLineStation);
        }

        // A-B -> A-B=N
        if (isNewStation(downLineStation) && isDownEndStation(upLineStation)) {
            graph.addVertex(downLineStation);
            linkStation(upLineStation, downLineStation, distance);
            return List.of(downLineStation);
        }

        // A-B -> N=A-B
        if (isNewStation(upLineStation) && isUpEndStation(downLineStation)) {
            graph.addVertex(upLineStation);
            linkStation(upLineStation, downLineStation, distance);
            return List.of(upLineStation);
        }

        // A-B -> A=N-B
        if (isNewStation(downLineStation) && !isDownEndStation(upLineStation)) {
            Station upLineNextStation = findNextStation(upLineStation);
            addStationToUpLine(upLineStation, downLineStation, upLineNextStation, distance);
            return List.of(downLineStation);
        }

        // A-B -> A-N=B
        if (isNewStation(upLineStation) && !isUpEndStation(downLineStation)) {
            Station previousStation = findPreviousStation(downLineStation);
            addStationToDownLine(previousStation, upLineStation, downLineStation, distance);
            return List.of(upLineStation);
        }

        throw new IllegalArgumentException("예외 발생");
    }

    private void validateStations(final Station upLineStation, final Station downLineStation) {
        if (upLineStation.equals(downLineStation)) {
            throw new IllegalArgumentException("서로 다른 역을 입력해 주세요.");
        }

        if (isNewStation(upLineStation) && isNewStation(downLineStation) && getStationSize() != 0) {
            throw new IllegalArgumentException("모두 새로운 역입니다. 새로운 역과 기존 역을 입력해 주세요.");
        }

        if (!isNewStation(upLineStation) && !isNewStation(downLineStation)) {
            throw new StationAlreadyExistException();
        }
    }

    public boolean isSameLine(Line line) {
        return this.line.equals(line);
    }

    private int findOrderOf(final Station station) {
        return findAllStationsInOrder().indexOf(station);
    }

    public EdgeEntity findEdgeEntity(Station station) {
        return new EdgeEntity(line.getId(), station.getId(), findOrderOf(station), findDistance(station));
    }

    private int findDistance(Station sourceStation, Station targetStation) {
        final DefaultWeightedEdge edge = graph.getEdge(sourceStation, targetStation);
        return (int) graph.getEdgeWeight(edge);
    }

    public Integer findDistance(Station station) {
        Set<DefaultWeightedEdge> outgoingEdges = graph.outgoingEdgesOf(station);
        if (outgoingEdges.isEmpty()) {
            return null;
        }
        return (int) graph.getEdgeWeight(outgoingEdges.iterator().next());
    }

    public Station findUpEndStation() {
        return graph.vertexSet().stream()
                .filter(vertex -> graph.incomingEdgesOf(vertex).isEmpty())
                .findAny()
                .orElseThrow(() -> new IllegalStateException("상행종점이 존재하지 않습니다."));
    }

    public Station findNextStation(Station station) {
        Set<DefaultWeightedEdge> outgoingEdges = graph.outgoingEdgesOf(station);
        if (!outgoingEdges.isEmpty()) {
            final DefaultWeightedEdge closestEdge = outgoingEdges.iterator().next();
            return graph.getEdgeTarget(closestEdge);
        }
        throw new IllegalStateException("다음 역이 존재하지 않습니다.");
    }

    private Station findPreviousStation(Station station) {
        final Set<DefaultWeightedEdge> defaultWeightedEdges = graph.incomingEdgesOf(station);
        if (!defaultWeightedEdges.isEmpty()) {
            final DefaultWeightedEdge closestEdge = defaultWeightedEdges.iterator().next();
            return graph.getEdgeSource(closestEdge);
        }
        throw new IllegalStateException("이전 역이 존재하지 않습니다.");
    }

    public List<Station> findAllStationsInOrder() {
        List<Station> allStationsInOrder = new ArrayList<>();

        Station upEndStation = findUpEndStation();
        while (upEndStation != null) {
            allStationsInOrder.add(upEndStation);
            Set<DefaultWeightedEdge> outgoingEdges = graph.outgoingEdgesOf(upEndStation);
            if (outgoingEdges.isEmpty()) {
                upEndStation = null;
            } else {
                upEndStation = graph.getEdgeTarget(outgoingEdges.iterator().next());
            }
        }
        return allStationsInOrder;
    }

    private boolean isUpEndStation(Station station) {
        int inDegree = graph.inDegreeOf(station);
        int outDegree = graph.outDegreeOf(station);

        return inDegree == 0 && outDegree == 1;
    }

    private boolean isDownEndStation(Station station) {
        int inDegree = graph.inDegreeOf(station);
        int outDegree = graph.outDegreeOf(station);

        return inDegree == 1 && outDegree == 0;
    }

    private boolean isNewStation(Station station) {
        return !graph.containsVertex(station);
    }

    private void linkStation(final Station sourceStation, final Station targetStation, final int distance) {
        graph.setEdgeWeight(graph.addEdge(sourceStation, targetStation), distance);
    }

    private void addStationToDownLine(final Station targetPreviousStation, final Station newStation, final Station targetStation, final int distance) {
        int originDistance = findDistance(targetPreviousStation, targetStation);
        validateAddDistance(distance, originDistance);

        graph.addVertex(newStation);
        final int remainDistance = originDistance - distance;
        graph.removeEdge(targetPreviousStation, targetStation);
        linkStation(newStation, targetStation, distance);
        linkStation(targetPreviousStation, newStation, remainDistance);
    }

    private void addStationToUpLine(final Station sourceStation, final Station newStation, final Station sourceNextStation, final int distance) {
        int originDistance = findDistance(sourceStation, sourceNextStation);
        validateAddDistance(distance, originDistance);

        graph.addVertex(newStation);
        final int remainDistance = originDistance - distance;
        graph.removeEdge(sourceStation, sourceNextStation);
        linkStation(sourceStation, newStation, distance);
        linkStation(newStation, sourceNextStation, remainDistance);
    }

    private void validateAddDistance(int distance, int originDistance) {
        if (distance >= originDistance) {
            throw new IllegalArgumentException("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }
    }

    public List<Station> clear() {
        List<Station> allStationsInOrder = findAllStationsInOrder();
        graph.removeAllVertices(allStationsInOrder);
        return allStationsInOrder;
    }

    public int getStationSize() {
        return graph.vertexSet().size();
    }

    public void delete(Station station) {
        if (graph.vertexSet().size() == 2) {
            throw new IllegalStateException("한개의 역만 지울 수 없음");
        }
        List<DefaultWeightedEdge> adjacentEdges = new ArrayList<>(graph.edgesOf(station));

        if (adjacentEdges.size() == 2) {
            removeMiddleStation(station);
        }

        if (adjacentEdges.size() == 1) {
            graph.removeEdge(adjacentEdges.get(0));
            graph.removeVertex(station);
        }

    }

    private void removeMiddleStation(Station station) {
        Station previousStation = findPreviousStation(station);
        Station nextStation = findNextStation(station);

        int distanceWithPrevious = findDistance(previousStation);
        int distanceWithNext = findDistance(station);

        int distance = distanceWithPrevious + distanceWithNext;

        graph.removeEdge(previousStation, station);
        graph.removeEdge(station, nextStation);
        graph.removeVertex(station);

        linkStation(previousStation, nextStation, distance);
    }

    public Optional<Station> findStationByName(String name) {
        return graph.vertexSet().stream()
                .filter(s -> s.isSameName(name))
                .findAny();
    }

    public boolean isStationExist(Station station) {
        return graph.vertexSet().contains(station);
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> getMultiGraph() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : this.graph.vertexSet()) {
            graph.addVertex(station);
        }

        for (DefaultWeightedEdge defaultWeightedEdge : this.graph.edgeSet()) {
            Station source = this.graph.getEdgeSource(defaultWeightedEdge);
            Station target = this.graph.getEdgeTarget(defaultWeightedEdge);

            graph.setEdgeWeight(graph.addEdge(source, target), findDistance(source));
        }

        return graph;
    }
}