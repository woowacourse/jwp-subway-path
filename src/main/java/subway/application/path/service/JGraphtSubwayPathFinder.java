package subway.application.path.service;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.path.SubwayPath;
import subway.domain.path.SubwayPathFinder;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

@Service
public class JGraphtSubwayPathFinder implements SubwayPathFinder {
    @Override
    public SubwayPath findShortestPath(final List<Line> allLines, final Station from, final Station to) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> stations = initGraph(allLines);

        validateIsExistStations(stations, from, to);
        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraPath
                = new DijkstraShortestPath<>(stations);

        final GraphPath<Station, DefaultWeightedEdge> dijkstraPathPath = dijkstraPath.getPath(from, to);

        validateIsExistPath(from, to, dijkstraPathPath);
        final List<Station> shortestPath = dijkstraPathPath.getVertexList();
        final int distance = (int) dijkstraPath.getPathWeight(from, to);

        return new SubwayPath(shortestPath, distance);
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> initGraph(final List<Line> allLines) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> stationsGraph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (final Line allLine : allLines) {
            final Sections sections = allLine.getSections();
            addSection(stationsGraph, sections);
        }

        return stationsGraph;
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

    private void validateIsExistStations(
            final WeightedMultigraph<Station, DefaultWeightedEdge> stations,
            final Station from,
            final Station to
    ) {
        if (!stations.containsVertex(from)) {
            throw new IllegalStateException("존재하지 않는 역 입니다: " + from.getStationName());
        }
        
        if (!stations.containsVertex(to)) {
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
}
