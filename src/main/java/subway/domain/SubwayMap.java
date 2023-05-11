package subway.domain;

import java.util.Map;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class SubwayMap {

    private final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> subwayMap;
    private final Lines lines;

    public SubwayMap(Lines lines) {
        this.subwayMap = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        this.lines = lines;
    }

    public void addStation(Station upStation, Station downStation, Integer distance) {
        subwayMap.addVertex(upStation);
        subwayMap.addVertex(downStation);
        DefaultWeightedEdge edge = subwayMap.addEdge(upStation, downStation);
        subwayMap.setEdgeWeight(edge, distance);
    }

    public void addStation(Line line, Station upStation, Station downStation, Integer distance) {
        subwayMap.addVertex(upStation);
        subwayMap.addVertex(downStation);
        DefaultWeightedEdge edge = subwayMap.addEdge(upStation, downStation);
        subwayMap.setEdgeWeight(edge, distance);

        Map<Long, Line> linesMap = lines.getLines();
        Line targetLine = linesMap.get(line.getId());

        if (targetLine.isInitState()) {
            targetLine.initStations(upStation, downStation);
            return;
        }

        if (targetLine.hasStation(upStation)) {
            targetLine.addLast(downStation);
            return;
        }

        targetLine.addBeforeAt(upStation, downStation);
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