package subway.domain.path;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import subway.domain.core.Section;
import subway.domain.core.Sections;
import subway.domain.core.Station;

public class PathFinder {
	private Sections sections;

	public DijkstraShortestPath<String, DefaultWeightedEdge> getDijkstraShortestPath() {
		final WeightedMultigraph<String, DefaultWeightedEdge> graph = drawGraph();

		return new DijkstraShortestPath<>(graph);
	}

	public WeightedMultigraph<String, DefaultWeightedEdge> drawGraph() {
		final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
			DefaultWeightedEdge.class);
		addVertex(graph);
		setEdgeWeight(graph);

		return graph;
	}

	private void addVertex(final WeightedMultigraph<String, DefaultWeightedEdge> graph) {
		final List<Station> stations = sections.getSortedStations();
		for (Station station : stations) {
			graph.addVertex(station.getName());
		}
	}

	private void setEdgeWeight(final WeightedMultigraph<String, DefaultWeightedEdge> graph) {
		final List<Section> allSection = sections.getSections();
		for (Section section : allSection) {
			String upStation = section.getUpStation().getName();
			String downStation = section.getDownStation().getName();
			graph.setEdgeWeight(
				graph.addEdge(upStation, downStation),
				section.getDistance());
		}
	}
}
