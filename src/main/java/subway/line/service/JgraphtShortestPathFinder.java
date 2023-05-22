package subway.line.service;

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
import subway.line.domain.FareCriteria;
import subway.line.domain.Line;
import subway.line.domain.LineNameKey;
import subway.line.dto.ShortestPathResponse;
import subway.line.dto.TraverseStationDto;
import subway.section.domain.Section;
import subway.station.domain.Station;

public class JgraphtShortestPathFinder implements ShortestPathFinder{

  private static final int DEFAULT_FARE = 1250;
  private static final int ADDITIONAL_FARE = 100;
  private final int DISTANCE_FROM_GAP_FROM_SECOND_TO_THIRD = SECOND_SECTION.getDistanceFrom() - FIRST_SECTION.getDistanceFrom();

  private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
  private final Map<LineNameKey, String> lineNames;

  public JgraphtShortestPathFinder() {
    this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    this.lineNames = new HashMap<>();
  }

  @Override
  public void makeGraph(final List<Line> lines) {
      for (Line line : lines) {
        makeGraphBySection(line);
      }
  }

  private void makeGraphBySection(final Line line) {
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

  @Override
  public ShortestPathResponse getShortestPathResponse(final Station fromStation, final Station toStation) {
    final GraphPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(
        graph).getPath(fromStation, toStation);
    final double distance = shortestPath.getWeight();
    return new ShortestPathResponse(distance, calculateFare((int) distance), getTraverseStationDtos(shortestPath));
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
    return (int) ((Math.ceil((double) (distance - 1) / fareCriteria.getDistancePer()) + 1) * ADDITIONAL_FARE);
  }

  private List<TraverseStationDto> getTraverseStationDtos(final GraphPath<Station, DefaultWeightedEdge> shortestPath) {
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
