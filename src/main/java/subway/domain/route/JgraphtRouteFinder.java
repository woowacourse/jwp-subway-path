package subway.domain.route;

import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.station.Stations;
import subway.event.StationChange;
import subway.service.LineQueryService;

@Component
public class JgraphtRouteFinder implements RouteFinder {

  private DijkstraShortestPath<Station, EdgeSection> dijkstraGraph;
  private final LineQueryService lineQueryService;

  public JgraphtRouteFinder(final LineQueryService lineQueryService) {
    this.lineQueryService = lineQueryService;
  }

  @EventListener({ContextRefreshedEvent.class, StationChange.class})
  public void updateDijkstraGraph() {
    final List<Line> lines = lineQueryService.searchAllLine();

    Graph<Station, EdgeSection> graph
        = new WeightedMultigraph<>(EdgeSection.class);

    for (final Line line : lines) {
      makeGraphFromSections(graph, line);
    }

    dijkstraGraph = new DijkstraShortestPath<>(graph);
  }

  private void makeGraphFromSections(
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

  private void addVertex(
      final Graph<Station, EdgeSection> graph,
      final String startStation,
      final String endStation
  ) {
    graph.addVertex(new Station(startStation));
    graph.addVertex(new Station(endStation));
  }

  private void addEdge(
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

  public List<Station> findShortestRoute(final Station departure, final Station arrival) {
    return dijkstraGraph.getPath(departure, arrival).getVertexList();
  }

  public Distance findShortestRouteDistance(final Station departure, final Station arrival) {
    return new Distance((int) dijkstraGraph.getPathWeight(departure, arrival));
  }

  public List<EdgeSection> findShortestRouteSections(final Station departure,
      final Station arrival) {
    return dijkstraGraph.getPath(departure, arrival).getEdgeList();
  }
}
