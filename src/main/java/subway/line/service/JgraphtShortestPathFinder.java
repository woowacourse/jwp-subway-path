package subway.line.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Line;
import subway.line.domain.LineNameKey;
import subway.line.dto.ShortestPathResponse;
import subway.line.dto.TraverseStationDto;
import subway.section.domain.Section;
import subway.station.domain.Station;

public class JgraphtShortestPathFinder implements ShortestPathFinder{

  private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
  private final Map<LineNameKey, String> lineNames;

  public JgraphtShortestPathFinder() {
    this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    this.lineNames = new HashMap<>();
  }

  @Override
  public void addGraph(final List<Line> lines) {
      for (Line line : lines) {
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
  }

  @Override
  public ShortestPathResponse getShortestPathResponse(final Station fromStation, final Station toStation) {
    final GraphPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(
        graph).getPath(fromStation, toStation);
    return new ShortestPathResponse(shortestPath.getWeight(), getTraverseStationDtos(shortestPath));
  }

  private List<TraverseStationDto> getTraverseStationDtos(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
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
