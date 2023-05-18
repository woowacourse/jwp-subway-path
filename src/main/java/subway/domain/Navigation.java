package subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.vo.Distance;

public class Navigation {

    private final Set<StationInformation> stationInformations = new HashSet<>();
    private final Graph<StationInformation, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
        DefaultWeightedEdge.class);
    private final ShortestPathAlgorithm<StationInformation, DefaultWeightedEdge> algorithm;

    public Navigation(final Lines lines) {
        addLines(lines);
        algorithm = new DijkstraShortestPath<>(graph);
    }

    private void addLines(final Lines lines) {
        for (final Line line : lines.getAllLine()) {
            final List<Section> allSection = line.getAllSection();
            addStationInfoToGraphAndStationsInfos(line, allSection);
        }
    }

    private void addStationInfoToGraphAndStationsInfos(final Line line, final List<Section> allSection) {
        for (final Section section : allSection) {
            final StationInformation upStationInformation = new StationInformation(section.getUpStation(), line);
            final StationInformation downStationInformation = new StationInformation(section.getDownStation(), line);
            final Distance distance = section.getDistance();

            graph.addVertex(upStationInformation);
            graph.addVertex(downStationInformation);

            stationInformations.add(upStationInformation);
            stationInformations.add(downStationInformation);

            graph.setEdgeWeight(graph.addEdge(upStationInformation, downStationInformation), distance.getValue());
        }
    }

    public Path findShortestPath(final Station start, final Station end) {
        final List<StationInformation> startStationInfos = stationInformations.stream()
            .filter(stationInformation -> stationInformation.isEqualStation(start))
            .collect(Collectors.toUnmodifiableList());
        final List<StationInformation> endStationInfos = stationInformations.stream()
            .filter(stationInformation -> stationInformation.isEqualStation(end))
            .collect(Collectors.toUnmodifiableList());
        final GraphPath<StationInformation, DefaultWeightedEdge> shortestPath = findShortestPath(
            startStationInfos, endStationInfos);
        final List<StationInformation> stationInformations = shortestPath.getVertexList();
        final long distance = (long) shortestPath.getWeight();
        return new Path(stationInformations, new Distance(distance));
    }

    private GraphPath<StationInformation, DefaultWeightedEdge> findShortestPath(
        final List<StationInformation> startStationInfos, final List<StationInformation> endStationInfos) {
        GraphPath<StationInformation, DefaultWeightedEdge> shortestPath = null;
        for (final StationInformation stationInfo : startStationInfos) {
            for (final StationInformation endStationInfo : endStationInfos) {
                final GraphPath<StationInformation, DefaultWeightedEdge> path =
                    algorithm.getPath(stationInfo, endStationInfo);
                shortestPath = judgeShortest(shortestPath, path);
            }
        }
        return shortestPath;
    }

    private GraphPath<StationInformation, DefaultWeightedEdge> judgeShortest(
        GraphPath<StationInformation, DefaultWeightedEdge> shortestPath,
        final GraphPath<StationInformation, DefaultWeightedEdge> path) {
        if (path == null) {
            return shortestPath;
        }
        if (shortestPath == null) {
            return path;
        }
        if (path.getWeight() < shortestPath.getWeight()) {
            shortestPath = path;
        }
        return shortestPath;
    }
}
