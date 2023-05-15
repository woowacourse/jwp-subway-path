package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Set;

public interface Graph {
    boolean isDownLastStation(Station station);

    boolean isUpFirstStation(Station station);

    void addVertex(Station station);

    DefaultWeightedEdge addEdge(Station upStation, Station downStation);

    void setEdgeWeight(DefaultWeightedEdge edge, int distance);

    boolean containsVertex(Station station);

    int inDegreeOf(Station station);

    int outDegreeOf(Station station);

    Set<DefaultWeightedEdge> outgoingEdgesOf(Station station);

    Set<DefaultWeightedEdge> incomingEdgesOf(Station station);

    Set<Station> vertexSet();

    Station getEdgeTarget(DefaultWeightedEdge edge);

    double getEdgeWeight(DefaultWeightedEdge edge);

    DefaultWeightedEdge getEdge(Station upStation, Station downStation);

    Station getEdgeSource(DefaultWeightedEdge edge);

    void removeEdge(Station upStation, Station downStation);

    void removeEdge(DefaultWeightedEdge edge);

    Set<DefaultWeightedEdge> edgesOf(Station station);

    void removeVertex(Station station);

    void removeAllEdges(Set<DefaultWeightedEdge> edges);
}
