package subway.domain.path;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

public class SubwayMap {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> stationsGraph;

    private SubwayMap(final WeightedMultigraph<Station, DefaultWeightedEdge> stationsGraph) {
        this.stationsGraph = stationsGraph;
    }

    public static SubwayMap from(final List<Line> allLines) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> stationsGraph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (final Line allLine : allLines) {
            final Sections sections = allLine.getSections();
            addSection(stationsGraph, sections);
        }

        return new SubwayMap(stationsGraph);
    }

    private static void addSection(
            final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
            final Sections sections
    ) {
        for (final Section section : sections.getSections()) {
            final Station firstStation = section.getFirstStation();
            final Station secondStation = section.getSecondStation();
            graph.addVertex(firstStation);
            graph.addVertex(secondStation);

            graph.setEdgeWeight(graph.addEdge(firstStation, secondStation), section.getDistance().getDistance());
        }
    }

    public SubwayPath findShortestPath(final Station from, final Station to) {
        validateIsExistStations(from, to);
        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraPath
                = new DijkstraShortestPath<>(stationsGraph);

        final GraphPath<Station, DefaultWeightedEdge> dijkstraPathPath = dijkstraPath.getPath(from, to);

        validateIsExistPath(from, to, dijkstraPathPath);
        final List<Station> shortestPath = dijkstraPathPath.getVertexList();
        final int distance = (int) dijkstraPath.getPathWeight(from, to);

        return new SubwayPath(shortestPath, distance);
    }

    private void validateIsExistStations(final Station from, final Station to) {
        if (!stationsGraph.containsVertex(from)) {
            throw new IllegalStateException("존재하지 않는 역 입니다: " + from.getStationName());
        }
        if (!stationsGraph.containsVertex(to)) {
            throw new IllegalStateException("존재하지 않는 역 입니다: " + to.getStationName());
        }
    }

    private void validateIsExistPath(
            final Station from,
            final Station to,
            final GraphPath<Station, DefaultWeightedEdge> dijkstraPathPath
    ) {
        if (dijkstraPathPath == null) {
            throw new IllegalStateException(
                    "연결 되지 않는 역 정보입니다: " + from.getStationName() + ", " + to.getStationName()
            );
        }
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> getStationsGraph() {
        return stationsGraph;
    }
}
