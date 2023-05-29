package subway.application.path.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
        final WeightedMultigraph<Station, PassingLineEdge> stations = initGraph(allLines);

        validateIsExistStations(stations, from, to);
        final DijkstraShortestPath<Station, PassingLineEdge> dijkstraPath
                = new DijkstraShortestPath<>(stations);

        final GraphPath<Station, PassingLineEdge> dijkstraPathPath = dijkstraPath.getPath(from, to);

        validateIsExistPath(from, to, dijkstraPathPath);
        final List<Station> shortestPath = dijkstraPathPath.getVertexList();
        final int distance = (int) dijkstraPath.getPathWeight(from, to);

        final Set<Line> passingLineSet = collectPassingLine(dijkstraPathPath);
        return new SubwayPath(shortestPath, distance, passingLineSet);
    }

    private Set<Line> collectPassingLine(final GraphPath<Station, PassingLineEdge> dijkstraPathPath) {
        return dijkstraPathPath.getEdgeList().stream()
                .map(PassingLineEdge::getPassingLine)
                .collect(Collectors.toSet());
    }

    private WeightedMultigraph<Station, PassingLineEdge> initGraph(final List<Line> allLines) {
        final WeightedMultigraph<Station, PassingLineEdge> stationsGraph
                = new WeightedMultigraph<>(PassingLineEdge.class);
        for (final Line line : allLines) {
            addSection(stationsGraph, line);
        }

        return stationsGraph;
    }

    private void addSection(
            final WeightedMultigraph<Station, PassingLineEdge> graph,
            final Line line
    ) {
        final Sections sections = line.getSections();
        for (final Section section : sections.getSections()) {
            final Station firstStation = section.getFirstStation();
            final Station secondStation = section.getSecondStation();
            graph.addVertex(firstStation);
            graph.addVertex(secondStation);

            final PassingLineEdge passingLineEdge = new PassingLineEdge(line);
            graph.addEdge(firstStation, secondStation, passingLineEdge);
            graph.setEdgeWeight(passingLineEdge, section.getDistance().getDistance());
        }
    }

    private void validateIsExistStations(
            final WeightedMultigraph<Station, PassingLineEdge> stations,
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
            final GraphPath<Station, PassingLineEdge> dijkstraPathPath
    ) {
        if (dijkstraPathPath == null) {
            throw new IllegalStateException(
                    "연결 되지 않는 역 정보입니다: " + from.getStationName() + ", " + to.getStationName()
            );
        }
    }

    private static class PassingLineEdge extends DefaultWeightedEdge {
        private final Line passingLine;

        public PassingLineEdge(final Line passingLine) {
            this.passingLine = passingLine;
        }

        public Line getPassingLine() {
            return passingLine;
        }
    }
}
