package subway.business.domain.subwaymap;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import subway.business.domain.line.Line;
import subway.business.domain.line.Section;
import subway.business.domain.line.Station;
import subway.business.domain.line.Stations;
import subway.business.domain.transfer.Transfer;

public class JgraphtSubwayMap implements SubwayMap {
    private final ShortestPathAlgorithm<Station, StationEdge> pathAlgorithm;

    private JgraphtSubwayMap(ShortestPathAlgorithm<Station, StationEdge> pathAlgorithm) {
        this.pathAlgorithm = pathAlgorithm;
    }

    public static JgraphtSubwayMap of(List<Line> lines, List<Transfer> transfers) {
        Graph<Station, StationEdge> graph = new DefaultDirectedWeightedGraph<>(
                StationEdge.class);
        addLinesToGraph(lines, graph);
        addTransfers(transfers, graph);
        return new JgraphtSubwayMap(new DijkstraShortestPath<>(graph));
    }

    @Override
    public List<Stations> calculateShortestPath(Station sourceStation, Station targetStation) {
        validateSameStation(sourceStation, targetStation);
        GraphPath<Station, StationEdge> graphPath = pathAlgorithm.getPath(sourceStation, targetStation);
        List<StationEdge> edgesOfPath = graphPath.getEdgeList();

        List<Station> stationList = new ArrayList<>();
        List<Stations> stationsListOfPath = new ArrayList<>();

        while (!edgesOfPath.isEmpty()) {
            StationEdge edge = popFirstEdge(edgesOfPath);
            stationList.add(edge.getSource());
            if (edge.getWeight() == 0) {
                stationsListOfPath.add(new Stations(stationList));
                stationList = new ArrayList<>();
            }
            if (edgesOfPath.isEmpty()) {
                stationList.add(edge.getTarget());
                stationsListOfPath.add(new Stations(stationList));
            }
        }
        return stationsListOfPath;
    }

    @Override
    public int calculateFareOfPath(Station sourceStation, Station targetStation) {
        validateSameStation(sourceStation, targetStation);
        int weightSum = (int) pathAlgorithm.getPathWeight(sourceStation, targetStation);
        // TODO 요금 정책 적용하며 리팩터링
        if (weightSum < 10) {
            return 1_250;
        }
        if (weightSum <= 50) {
            return 1_250 + ((weightSum - 10) / 5 * 100);
        }
        return 2_050 + ((weightSum - 50) / 8 * 100);
    }

    private void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(String.format("출발 역과 도착 역이 같아 경로를 계산할 수 없습니다. "
                    + "(입력 받은 역 : %s)", sourceStation.getName()));
        }
    }

    private static void addLinesToGraph(List<Line> lines, Graph<Station, StationEdge> graph) {
        for (Line line : lines) {
            addVertices(graph, line);
            addEdges(graph, line);
        }
    }

    private static void addVertices(Graph<Station, StationEdge> graph, Line line) {
        for (Station station : line.getOrderedStations().getStations()) {
            graph.addVertex(station);
        }
    }

    private static void addEdges(Graph<Station, StationEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            addUpwardEdgeToGraph(graph, section);
            addDownwardEdgeToGraph(graph, section);
        }
    }

    private static void addTransfers(List<Transfer> transfers,
                                     Graph<Station, StationEdge> graph) {
        for (Transfer transfer : transfers) {
            addUpwardTransferEdge(graph, transfer);
            addDownwardTransferEdge(graph, transfer);
        }
    }

    private static void addDownwardTransferEdge(Graph<Station, StationEdge> graph, Transfer transfer) {
        graph.setEdgeWeight(
                graph.addEdge(transfer.getLastStation(), transfer.getFirstStation()),
                0
        );
    }

    private static void addUpwardTransferEdge(Graph<Station, StationEdge> graph, Transfer transfer) {
        graph.setEdgeWeight(
                graph.addEdge(transfer.getFirstStation(), transfer.getLastStation()),
                0
        );
    }

    private static void addUpwardEdgeToGraph(Graph<Station, StationEdge> graph,
                                             Section section) {
        graph.setEdgeWeight(
                graph.addEdge(section.getUpwardStation(), section.getDownwardStation()),
                section.getDistance()
        );
    }

    private static void addDownwardEdgeToGraph(Graph<Station, StationEdge> graph,
                                               Section section) {
        graph.setEdgeWeight(
                graph.addEdge(section.getDownwardStation(), section.getUpwardStation()),
                section.getDistance()
        );
    }

    private StationEdge popFirstEdge(List<StationEdge> edgesOfPath) {
        return edgesOfPath.remove(0);
    }
}
