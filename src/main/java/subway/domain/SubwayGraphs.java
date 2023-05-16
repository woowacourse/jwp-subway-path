package subway.domain;

import org.springframework.stereotype.Component;
import subway.dto.LineDto;
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


    // TODO: 반환 타입 어떤걸로
    public LineDto createLine(Line line, Station upLineStation, Station downLineStation, int distance) {
        final SubwayGraph newLineGraph = new SubwayGraph(line);
        newLineGraph.createInitStations(upLineStation, downLineStation, distance);
        final List<Station> allStationsInOrder = newLineGraph.findAllStationsInOrder();
        subwayGraphs.add(newLineGraph);
        return new LineDto(line, allStationsInOrder);
    }

    public EdgeEntity findEdge(Line line, Station station) {
        final SubwayGraph subwayGraph = findSubwayGraphOf(line);
        return subwayGraph.findEdgeEntity(station);
    }

    public List<Station> findAllStationsInOrderOf(Line line) {
        final SubwayGraph subwayGraph = findSubwayGraphOf(line);
        return subwayGraph.findAllStationsInOrder();
    }

    public LineDto addStation(Line line, Station upLineStation, Station downLineStation, int distance) {
        final SubwayGraph lineGraph = findSubwayGraphOf(line);

        lineGraph.addStation(upLineStation, downLineStation, distance);
        final List<Station> allStationsInOrder = lineGraph.findAllStationsInOrder();
        return new LineDto(line, allStationsInOrder);
    }

    public Station createStation(final Line line, final Station upLineStation, final Station downLineStation, final int distance) {
        final SubwayGraph subwayGraph = findSubwayGraphOf(line);

        return subwayGraph.addStation(upLineStation, downLineStation, distance);
    }

    private SubwayGraph findSubwayGraphOf(final Line line) {
        final SubwayGraph lineGraph = subwayGraphs.stream()
                .filter(s -> s.isSameLine(line))
                .findFirst()
                .orElseThrow(() -> new LineNotFoundException());
        return lineGraph;
    }

    public Optional<Station> findStationByName(Line line, String name) {
        final SubwayGraph subwayGraph = findSubwayGraphOf(line);
        return subwayGraph.findStationByName(name);
    }

    public boolean isStationExistInAnyLine(Station station) {
        return subwayGraphs.stream()
                .anyMatch(subwayGraph -> subwayGraph.isStationExist(station));
    }

    public void remove(Line line) {
        SubwayGraph subwayGraph = findSubwayGraphOf(line);
        subwayGraphs.remove(subwayGraph);
    }

    public void deleteStation(Line line, Station station) {
        SubwayGraph subwayGraph = findSubwayGraphOf(line);
        subwayGraph.delete(station);
    }

    public List<Station> deleteAll(Line line) {
        SubwayGraph subwayGraph = findSubwayGraphOf(line);
        List<Station> removedStations = subwayGraph.clear();
        subwayGraphs.remove(subwayGraph);
        return removedStations;
    }
}