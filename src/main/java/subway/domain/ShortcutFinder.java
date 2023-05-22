package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.application.exception.NoSuchStationException;
import subway.domain.calculator.DefaultFee;
import subway.domain.calculator.FeeCalculator;
import subway.domain.vo.Section;
import subway.domain.vo.Shortcut;
import subway.domain.vo.Station;

import java.util.List;

public class ShortcutFinder {
    WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public ShortcutFinder(List<Section> sections) {
        this.graph = makeGraph(sections);
    }

    public Shortcut findShortcut(long departure, long arrival) {
        final FeeCalculator feeCalculator = new DefaultFee();
        final DijkstraShortestPath<Station, DefaultWeightedEdge> shortcut = new DijkstraShortestPath<>(graph);

        final Station departureStation = graph.vertexSet().stream()
                .filter(station -> station.getId().equals(departure))
                .findAny().orElseThrow(() -> new NoSuchStationException("해당 출발역이 없습니다."));

        final Station arrivalStation = graph.vertexSet().stream()
                .filter(station -> station.getId().equals(arrival))
                .findAny().orElseThrow(() -> new NoSuchStationException("해당 도착역이 없습니다."));

        final List<Station> path = shortcut.getPath(departureStation, arrivalStation).getVertexList();
        double distance = shortcut.getPathWeight(departureStation, arrivalStation);
        final int fee = feeCalculator.calculate(distance);

        return new Shortcut(path, fee);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Section section : sections) {
            stationGraph.addVertex(section.getDeparture());
            stationGraph.addVertex(section.getArrival());
        }
        for (Section section : sections) {
            stationGraph.setEdgeWeight(stationGraph.addEdge(section.getDeparture(), section.getArrival()), section.getDistanceValue());
        }
        return stationGraph;
    }
}
