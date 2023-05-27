package subway.domain.subway;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import subway.exception.LinesEmptyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Path {

	private WeightedMultigraph<String, DefaultWeightedEdge> graph;
	private final Lines lines;

	private Path(final Lines lines) {
		graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		this.lines = lines;
	}

	public static Path from(final Lines lines) {
		return new Path(lines);
	}

	public static Path createDefault() {
		return new Path(null);
	}

	public List<Station> findShortestPath(final String start, final String destination) {
		validateLineEmpty();
		initGraph();
		GraphPath<String, DefaultWeightedEdge> shortestPath = calculateShortestPath(start, destination);
		return findStationsFromPath(shortestPath);
	}

	private void validateLineEmpty() {
		if (lines == null || lines.isEmptyLines()) {
			throw new LinesEmptyException();
		}
	}

	private void initGraph() {
		graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

		for (Line line : lines.getLines()) {
			fillGraph(line);
		}
	}

	private void fillGraph(final Line line) {
		for (Section section : line.getSections()) {
			String upStation = section.getUpStation().getName();
			String downStation = section.getDownStation().getName();

			graph.addVertex(upStation);
			graph.addVertex(downStation);

			graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
		}
	}

	private GraphPath<String, DefaultWeightedEdge> calculateShortestPath(final String start, final String destination) {
		DijkstraShortestPath<String, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
		return shortestPath.getPath(start, destination);
	}

	private List<Station> findStationsFromPath(final GraphPath<String, DefaultWeightedEdge> path) {
		Map<String, Station> stationsByName = lines.getStationByNameKey();
		List<Station> stations = new ArrayList<>();

		for (String stationName : path.getVertexList()) {
			Station station = stationsByName.get(stationName);
			stations.add(station);
		}

		return stations;
	}

	public List<Set<String>> findShortestTransferLines(final String start, final String destination) {
		validateLineEmpty();
		initGraph();
		GraphPath<String, DefaultWeightedEdge> shortestPath = calculateShortestPath(start, destination);
		return findTransferLines(shortestPath);
	}

	private List<Set<String>> findTransferLines(final GraphPath<String, DefaultWeightedEdge> path) {
		Map<String, Station> stationsByName = lines.getStationByNameKey();
		List<Set<String>> transferLines = new ArrayList<>();

		for (String stationName : path.getVertexList()) {
			Station station = stationsByName.get(stationName);
			transferLines.add(lines.getAllLineNames(station));
		}

		return transferLines;
	}
}
