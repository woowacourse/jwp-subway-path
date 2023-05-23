package subway.domain.route;

import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Distance;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Stations;

public class JgraphtRouteFinder implements RouteFinder {

  private final DijkstraShortestPath<String, EdgeSection> dijkstraGraph;

  public JgraphtRouteFinder(final List<Line> lines) {
    Graph<String, EdgeSection> graph
        = new WeightedMultigraph<>(EdgeSection.class);

    for (final Line line : lines) {
      final List<Section> sections = line.getSections();
      makeGraphFromSections(graph, line);
    }

    dijkstraGraph = new DijkstraShortestPath<>(graph);
  }

  private void makeGraphFromSections(
      final Graph<String, EdgeSection> graph,
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

  private void addVertex(
      final Graph<String, EdgeSection> graph,
      final String startStation,
      final String endStation
  ) {
    graph.addVertex(startStation);
    graph.addVertex(endStation);
  }

  private void addEdge(
      final Graph<String, EdgeSection> graph, final String startStation,
      final String endStation, final int distance, final String lineName
  ) {
    final EdgeSection start = new EdgeSection(startStation, endStation, distance, lineName);
    final EdgeSection end = new EdgeSection(endStation, startStation, distance, lineName);

    graph.addEdge(startStation, endStation, start);
    graph.addEdge(endStation, startStation, end);

    graph.setEdgeWeight(start, distance);
    graph.setEdgeWeight(end, distance);
  }

  @Override
  public List<String> findShortestRoute(final String startStation, final String endStation) {
    return dijkstraGraph.getPath(startStation, endStation).getVertexList();
  }

  @Override
  public Distance findShortestRouteDistance(final String startStation, final String endStation) {
    return new Distance((int) dijkstraGraph.getPathWeight(startStation, endStation));
  }

  @Override
  public List<EdgeSection> findShortestRouteSections(final String startStation,
      final String endStation) {
    return dijkstraGraph.getPath(startStation, endStation).getEdgeList();
  }
}
