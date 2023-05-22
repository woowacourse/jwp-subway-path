package subway.jgrapht;

import java.util.Collection;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import subway.domain.Distance;
import subway.domain.Path;
import subway.domain.PathFinder;
import subway.domain.Section;
import subway.domain.Station;
import subway.error.exception.SectionConnectionException;

@Component
public class JgraphtPathFinder implements PathFinder {

	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

	private DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

	public JgraphtPathFinder(final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	@Override
	public void initialize(List<Section> sections) {
		addVertex(sections, graph);
		addEdgeWeight(sections, graph);

		shortestPath = new DijkstraShortestPath<>(graph);
	}

	@Override
	public Path findShortestPath(final Station departure, final Station arrival) {
		final GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(departure, arrival);

		final List<Station> stations = path.getVertexList();
		final Distance distance = new Distance((int)path.getWeight());

		return new Path(stations, distance);
	}

	private void addVertex(final List<Section> sections,
		final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
		sections.stream()
			.map(section -> List.of(section.getDeparture(), section.getArrival()))
			.flatMap(Collection::stream)
			.distinct()
			.forEach(graph::addVertex);
	}

	private void addEdgeWeight(final List<Section> sections,
		final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
		try {
			setEdgeWeights(sections, graph);
		} catch (IllegalArgumentException e) {
			throw new SectionConnectionException("연결된 구간이 없습니다.");
		}
	}

	private void setEdgeWeights(final List<Section> sections,
		final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
		for (final Section section : sections) {
			final DefaultWeightedEdge defaultWeightedEdge = graph.addEdge(section.getDeparture(), section.getArrival());
			final Distance distance = section.getDistance();

			graph.setEdgeWeight(defaultWeightedEdge, distance.getValue());
		}
	}

}
