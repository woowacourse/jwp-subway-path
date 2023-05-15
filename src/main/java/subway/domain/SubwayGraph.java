package subway.domain;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SubwayGraph implements Graph {

    private final DefaultDirectedWeightedGraph<Station, DefaultWeightedEdge> graph
            = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

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
    public void addVertex(final Station station) {
        graph.addVertex(station);
    }

    @Override
    public DefaultWeightedEdge addEdge(final Station upStation, final Station downStation) {
        graph.addEdge(upStation, downStation);
        return graph.getEdge(upStation, downStation);
    }

    @Override
    public void setEdgeWeight(final DefaultWeightedEdge edge, final int distance) {
        graph.setEdgeWeight(edge, distance);
    }

    @Override
    public boolean containsVertex(final Station station) {
        return graph.containsVertex(station);
    }

    @Override
    public int inDegreeOf(final Station station) {
        return graph.inDegreeOf(station);
    }

    @Override
    public int outDegreeOf(final Station station) {
        return graph.outDegreeOf(station);
    }

    @Override
    public Set<DefaultWeightedEdge> outgoingEdgesOf(final Station station) {
        return graph.outgoingEdgesOf(station);
    }

    @Override
    public Set<DefaultWeightedEdge> incomingEdgesOf(final Station station) {
        return graph.incomingEdgesOf(station);
    }

    @Override
    public Set<Station> vertexSet() {
        return graph.vertexSet();
    }

    @Override
    public Station getEdgeTarget(final DefaultWeightedEdge edge) {
        return graph.getEdgeTarget(edge);
    }

    @Override
    public double getEdgeWeight(final DefaultWeightedEdge edge) {
        return graph.getEdgeWeight(edge);
    }

    @Override
    public DefaultWeightedEdge getEdge(final Station upStation, final Station downStation) {
        return graph.getEdge(upStation, downStation);
    }

    @Override
    public Station getEdgeSource(final DefaultWeightedEdge edge) {
        return graph.getEdgeSource(edge);
    }

    @Override
    public void removeEdge(final Station upStation, final Station downStation) {
        graph.removeEdge(upStation, downStation);
    }

    @Override
    public void removeEdge(final DefaultWeightedEdge edge) {
        graph.removeEdge(edge);
    }

    @Override
    public Set<DefaultWeightedEdge> edgesOf(final Station station) {
        return graph.edgesOf(station);
    }

    @Override
    public void removeVertex(final Station station) {
        graph.removeVertex(station);
    }

    @Override
    public void removeAllEdges(final Set<DefaultWeightedEdge> edges) {
        graph.removeAllEdges(edges);
    }
}
