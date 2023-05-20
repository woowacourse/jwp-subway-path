package subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.NoSuchShortestPathException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Path {

    private WeightedMultigraph<Station, LineRecordedDefaultWeightedEdge> graph;

    public Path(List<Line> lines, List<Station> stations) {
        graph = new WeightedMultigraph<>(LineRecordedDefaultWeightedEdge.class);
        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Line line : lines) {
            for (Map.Entry<Long, Section> sectionEntry : line.getSections().getSections().entrySet()) {
                Section section = sectionEntry.getValue();
                LineRecordedDefaultWeightedEdge lineRecordedDefaultWeightedEdge = new LineRecordedDefaultWeightedEdge(line.getId());
                graph.addEdge(section.getLeftStation(), section.getRightStation(), lineRecordedDefaultWeightedEdge);
                graph.setEdgeWeight(lineRecordedDefaultWeightedEdge, section.getDistance().getDistance());
            }
        }
    }

    public GraphPath<Station, LineRecordedDefaultWeightedEdge> getDijkstraShortestPath(Station startStation, Station endStation) {
        DijkstraShortestPath<Station, LineRecordedDefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);
        GraphPath<Station, LineRecordedDefaultWeightedEdge> path = dijkstraShortestPath.getPath(startStation, endStation);

        if (Objects.isNull(path)) {
            throw new NoSuchShortestPathException();
        }
        return path;
    }

    public Station getEdgeSource(LineRecordedDefaultWeightedEdge lineRecordedDefaultWeightedEdge) {
        return graph.getEdgeSource(lineRecordedDefaultWeightedEdge);
    }

    public Station getEdgeTarget(LineRecordedDefaultWeightedEdge lineRecordedDefaultWeightedEdge) {
        return graph.getEdgeTarget(lineRecordedDefaultWeightedEdge);
    }

    public int getEdgeWeight(LineRecordedDefaultWeightedEdge lineRecordedDefaultWeightedEdge) {
        return (int) graph.getEdgeWeight(lineRecordedDefaultWeightedEdge);
    }
}
