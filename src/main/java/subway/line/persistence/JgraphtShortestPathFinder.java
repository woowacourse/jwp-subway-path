package subway.line.persistence;

import static subway.line.domain.FareCriteria.FIRST_SECTION;
import static subway.line.domain.FareCriteria.SECOND_SECTION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.line.domain.FareCriteria;
import subway.line.domain.Line;
import subway.line.domain.LineNameKey;
import subway.line.domain.ShortestPathFinder;
import subway.line.dto.ShortestPathResponse;
import subway.line.dto.TraverseStationDto;
import subway.section.domain.Section;
import subway.station.domain.Station;

@Component
public class JgraphtShortestPathFinder implements ShortestPathFinder {

  private static final int DEFAULT_FARE = 1250;
  private static final int ADDITIONAL_FARE = 100;
  private final int DISTANCE_FROM_GAP_FROM_SECOND_TO_THIRD = SECOND_SECTION.getDistanceFrom() - FIRST_SECTION.getDistanceFrom();

  @Override
  public ShortestPathResponse getShortestPathResponse(final List<Line> lines, final Station fromStation, final Station toStation) {
    final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
        DefaultWeightedEdge.class);
    final Map<LineNameKey, String> lineNames = new HashMap<>();

    makeGraph(lines, graph, lineNames);

    final GraphPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(
        graph).getPath(fromStation, toStation);
    final double distance = shortestPath.getWeight();
    return new ShortestPathResponse(distance, calculateFare((int) distance), getTraverseStationDtos(shortestPath, lineNames));
  }

  private void makeGraph(final List<Line> lines, final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
      final Map<LineNameKey, String> lineNames) {
    for (Line line : lines) {
      makeGraphBySection(line, graph, lineNames);
    }
  }

  private void makeGraphBySection(final Line line, final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
      final Map<LineNameKey, String> lineNames) {
    for (Section section : line.getSections()) {
      final Station upStation = section.getUpStation();
      final Station downStation = section.getDownStation();

      lineNames.put(new LineNameKey(section.getUpStation().getId(), section.getDownStation().getId()),
          line.getLineName());
      graph.addVertex(upStation);
      graph.addVertex(downStation);
      graph.addEdge(upStation, downStation);
      graph.setEdgeWeight(upStation, downStation, section.getDistance());
    }
  }

  private int calculateFare(int distance) {
    int fare = DEFAULT_FARE;

    distance -= FIRST_SECTION.getDistanceFrom();
    if (distance < 0) {
      return fare;
    }

    if (distance <= DISTANCE_FROM_GAP_FROM_SECOND_TO_THIRD) {
      return fare + calculateFareByCriteria(distance, FIRST_SECTION);
    }

    fare += calculateFareByCriteria(DISTANCE_FROM_GAP_FROM_SECOND_TO_THIRD, FIRST_SECTION);
    distance -= DISTANCE_FROM_GAP_FROM_SECOND_TO_THIRD;
    return fare + calculateFareByCriteria(distance, SECOND_SECTION);
  }

  private int calculateFareByCriteria(final int distance, final FareCriteria fareCriteria) {
    return (int) ((Math.ceil((distance - 1) / fareCriteria.getDistancePer()) + 1) * ADDITIONAL_FARE);
  }

  private List<TraverseStationDto> getTraverseStationDtos(final GraphPath<Station, DefaultWeightedEdge> shortestPath,
      final Map<LineNameKey, String> lineNames) {
    final List<Station> vertexList = shortestPath.getVertexList();
    final List<TraverseStationDto> traverseStationDtos = new ArrayList<>();
    final int size = vertexList.size();

    for (int index = 0; index < size - 1; index++) {
      traverseStationDtos.add(
          new TraverseStationDto(
              lineNames.get(new LineNameKey(vertexList.get(index).getId(), vertexList.get(index + 1).getId())),
              vertexList.get(index).getName()));
    }
    traverseStationDtos.add(
        new TraverseStationDto(
            lineNames.get(new LineNameKey(vertexList.get(size - 2).getId(), vertexList.get(size - 1).getId())),
            vertexList.get(size - 1).getName()));
    return traverseStationDtos;
  }

}
