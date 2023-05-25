package subway.domain.route;

import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.station.Stations;

public class DijkstraGraphFactory {

  private DijkstraGraphFactory() {
  }

  public static DijkstraShortestPath<Station, EdgeSection> makeDijkstraGraph(
      final List<Line> lines) {
    Graph<Station, EdgeSection> graph
        = new WeightedMultigraph<>(EdgeSection.class);

    for (final Line line : lines) {
      makeGraphFromSections(graph, line);
    }

    return new DijkstraShortestPath<>(graph);
  }

  private static void makeGraphFromSections(
      final Graph<Station, EdgeSection> graph,
      final Line line
  ) {

    final List<Section> sections = line.getSections();

    for (final Section section : sections) {
      final Stations stations = section.getStations();

      final String startStation = stations.getCurrent().getName();
      final String endStation = stations.getNext().getName();
      final int distance = stations.getDistance();

      addVertex(graph, startStation, endStation);
      addEdge(graph, startStation, endStation, distance, line.getName());
    }
  }

  private static void addVertex(
      final Graph<Station, EdgeSection> graph,
      final String startStation,
      final String endStation
  ) {
    graph.addVertex(new Station(startStation));
    graph.addVertex(new Station(endStation));
  }

  private static void addEdge(
      final Graph<Station, EdgeSection> graph, final String startStation,
      final String endStation, final int distance, final String lineName
  ) {
    final EdgeSection start = new EdgeSection(startStation, endStation, distance, lineName);
    final EdgeSection end = new EdgeSection(endStation, startStation, distance, lineName);

    graph.addEdge(new Station(startStation), new Station(endStation), start);
    graph.addEdge(new Station(endStation), new Station(startStation), end);

    graph.setEdgeWeight(start, distance);
    graph.setEdgeWeight(end, distance);
  }
}
