package subway.util;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.section.Section;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PathUtil {
    public List<Section> getSectionsByShortestPath(final Station startStation,
                                                   final Station endStation,
                                                   final List<Line> lines) {
        return getShortestPath(startStation, endStation, lines).getEdgeList().stream()
                .map(edge -> Section.of(edge.getSource(), edge.getTarget(), Distance.from(edge.getDistance())))
                .collect(Collectors.toList());
    }

    public List<Station> getStationsByShortestPath(final Station startStation,
                                                   final Station endStation,
                                                   final List<Line> lines) {
        return getShortestPath(startStation, endStation, lines).getVertexList();
    }

    private GraphPath<Station, SectionWeightEdge> getShortestPath(
            final Station startStation,
            final Station endStation,
            final List<Line> lines) {
        final WeightedMultigraph<Station, SectionWeightEdge> graph = new WeightedMultigraph<>(SectionWeightEdge.class);

        for (Line line : lines) {
            updateAllStation(graph, line);
            updateAllSection(graph, line);
        }

        return new DijkstraShortestPath<>(graph).getPath(startStation, endStation);
    }

    private void updateAllStation(final WeightedMultigraph<Station, SectionWeightEdge> graph,
                                  final Line line) {
        final List<Station> stations = line.findStationsByOrdered();

        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void updateAllSection(final WeightedMultigraph<Station, SectionWeightEdge> graph,
                                  final Line line) {
        final List<Section> sections = line.getSections().getSections();

        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getDistance());
        }
    }
}
