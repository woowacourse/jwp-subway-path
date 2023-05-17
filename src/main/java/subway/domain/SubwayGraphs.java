package subway.domain;

import org.springframework.stereotype.Component;
import subway.entity.EdgeEntity;
import subway.exception.LineNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SubwayGraphs {
    private final List<SubwayGraph> subwayGraphs;

    public SubwayGraphs() {
        this.subwayGraphs = new ArrayList<>();
    }

    public void addLine(Line line) {
        SubwayGraph subwayGraph = new SubwayGraph(line);
        subwayGraphs.add(subwayGraph);
    }

    public EdgeEntity findEdge(Line line, Station station) {
        final SubwayGraph subwayGraph = findSubwayGraph(line);
        return subwayGraph.findEdgeEntity(station);
    }

    public List<Station> findAllStationsInOrder(Line line) {
        final SubwayGraph subwayGraph = findSubwayGraph(line);
        return subwayGraph.findAllStationsInOrder();
    }

    public List<Station> addStation(final Line line, final Station upLineStation, final Station downLineStation, final int distance) {
        final SubwayGraph subwayGraph = findSubwayGraph(line);
        return subwayGraph.addStation(upLineStation, downLineStation, distance);
    }

    public SubwayGraph findSubwayGraph(final Line line) {
        final SubwayGraph lineGraph = subwayGraphs.stream()
                .filter(s -> s.isSameLine(line))
                .findFirst()
                .orElseThrow(() -> new LineNotFoundException());
        return lineGraph;
    }

    public Optional<Station> findStationByName(Line line, String name) {
        final SubwayGraph subwayGraph = findSubwayGraph(line);
        return subwayGraph.findStationByName(name);
    }

    public boolean isStationExistInAnyLine(Station station) {
        return subwayGraphs.stream()
                .anyMatch(subwayGraph -> subwayGraph.isStationExist(station));
    }

    public List<Station> remove(Line line) {
        SubwayGraph subwayGraph = findSubwayGraph(line);
        List<Station> removedStations = subwayGraph.clear();
        subwayGraphs.remove(subwayGraph);
        return removedStations;
    }

    public void deleteStation(Line line, Station station) {
        SubwayGraph subwayGraph = findSubwayGraph(line);
        subwayGraph.delete(station);
    }
}