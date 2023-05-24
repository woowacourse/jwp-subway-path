package subway.domain.graph;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Direction;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static subway.domain.Direction.UP;

public class SubwayGraph implements Graph {

    private final DefaultDirectedWeightedGraph<Station, DefaultWeightedEdge> graph
            = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    @Override
    public void addStation(final Station station) {
        graph.addVertex(station);
    }

    @Override
    public Section addSection(final Station upStation,
                              final Station downStation,
                              final int distance) {
        final Section section = new Section(upStation, downStation, distance);
        graph.addEdge(upStation, downStation, section);
        graph.setEdgeWeight(section, section.getDistance());
        return section;
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
        return ((Section) edge).getDistance();
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

    @Override
    public boolean isTerminal(final Direction direction, final Station station) {
        if (direction == UP) {
            return isUpFirstStation(station);
        }
        return isDownLastStation(station);
    }

    @Override
    public List<Section> getSections() {
        final List<Section> collect = graph.edgeSet().stream()
                .map(it -> (Section) it)
                .collect(Collectors.toList());
        return new ArrayList<>(collect);
    }

    @Override
    public DefaultDirectedWeightedGraph<Station, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    @Override
    public List<Station> getStations() {
        return new ArrayList<>(graph.vertexSet());
    }

    private boolean isDownLastStation(final Station station) {
        int inDegree = graph.inDegreeOf(station);
        int outDegree = graph.outDegreeOf(station);

        return inDegree == 1 && outDegree == 0;
    }

    private boolean isUpFirstStation(final Station station) {
        int inDegree = graph.inDegreeOf(station);
        int outDegree = graph.outDegreeOf(station);

        return inDegree == 0 && outDegree == 1;
    }

    @Override
    public String toString() {
        return "SubwayGraph{" +
                "graph=" + graph +
                '}';
    }
}
