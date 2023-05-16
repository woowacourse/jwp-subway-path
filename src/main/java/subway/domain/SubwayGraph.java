package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SubwayGraph implements Graph {

    private final DefaultDirectedWeightedGraph<Station, DefaultWeightedEdge> graph
            = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    @Override
    public Graph getInstance() {
        return new SubwayGraph();
    }

    @Override
    public void createInitialSection(final Section section) {
        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();
        final int distance = section.getDistance();
        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.addEdge(upStation, downStation);
        graph.setEdgeWeight(graph.getEdge(upStation, downStation), distance);
    }

    @Override
    public boolean isDownLastStation(final Station station) {
        int inDegree = graph.inDegreeOf(station);
        int outDegree = graph.outDegreeOf(station);

        return inDegree == 1 && outDegree == 0;
    }

    @Override
    public boolean isUpFirstStation(final Station station) {
        int inDegree = graph.inDegreeOf(station);
        int outDegree = graph.outDegreeOf(station);

        return inDegree == 0 && outDegree == 1;
    }

    @Override
    public void addStation(final Station station) {
        graph.addVertex(station);
    }

    @Override
    public DefaultWeightedEdge addSection(final Station upStation, final Station downStation) {
        graph.addEdge(upStation, downStation);
        return graph.getEdge(upStation, downStation);
    }

    @Override
    public void setSectionDistance(final DefaultWeightedEdge section, final int distance) {
        graph.setEdgeWeight(section, distance);
    }

    @Override
    public boolean containsStation(final Station station) {
        return graph.containsVertex(station);
    }

    @Override
    public Set<DefaultWeightedEdge> downStationsOf(final Station station) {
        return graph.outgoingEdgesOf(station);
    }

    @Override
    public Set<DefaultWeightedEdge> upStationsOf(final Station station) {
        return graph.incomingEdgesOf(station);
    }

    @Override
    public Set<Station> stationSet() {
        return graph.vertexSet();
    }

    @Override
    public Station getDownStation(final DefaultWeightedEdge edge) {
        return graph.getEdgeTarget(edge);
    }

    @Override
    public double getSectionDistance(final DefaultWeightedEdge edge) {
        return graph.getEdgeWeight(edge);
    }

    @Override
    public DefaultWeightedEdge getSection(final Station upStation, final Station downStation) {
        return graph.getEdge(upStation, downStation);
    }

    @Override
    public Station getUpStation(final DefaultWeightedEdge edge) {
        return graph.getEdgeSource(edge);
    }

    @Override
    public void removeSection(final Station upStation, final Station downStation) {
        graph.removeEdge(upStation, downStation);
    }

    @Override
    public void removeSection(final DefaultWeightedEdge edge) {
        graph.removeEdge(edge);
    }

    @Override
    public Set<DefaultWeightedEdge> sectionsOf(final Station station) {
        return graph.edgesOf(station);
    }

    @Override
    public void removeStation(final Station station) {
        graph.removeVertex(station);
    }

    @Override
    public void removeAllSections(final Set<DefaultWeightedEdge> edges) {
        graph.removeAllEdges(edges);
    }
}
