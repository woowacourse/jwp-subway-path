package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import subway.domain.vo.Distance;

public class SubwayMap {

    private final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> subwayMap;

    public SubwayMap() {
        this.subwayMap = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }

    public void addStation(Station upStation, Station downStation, Distance distance) {
        subwayMap.addVertex(upStation);
        subwayMap.addVertex(downStation);
        DefaultWeightedEdge edge = subwayMap.addEdge(upStation, downStation);
        subwayMap.setEdgeWeight(edge, distance.getValue());
    }

    public boolean containsStation(Station station) {
        return subwayMap.containsVertex(station);
    }

    public boolean isNextStation(Station upStation, Station downStation) {
        return subwayMap.containsEdge(upStation, downStation);
    }

    public void deleteStation(Station station) {
        subwayMap.removeVertex(station);
    }
}