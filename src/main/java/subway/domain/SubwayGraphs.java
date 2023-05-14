package subway.domain;

import org.springframework.stereotype.Component;
import subway.dto.LineDto;
import subway.entity.EdgeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class SubwayGraphs {
    private final List<SubwayGraph> subwayGraphs;

    public SubwayGraphs() {
        this.subwayGraphs = new ArrayList<>();
    }

    public LineDto createLine(Line line, Station upLineStation, Station downLineStation, int distance) {
        final SubwayGraph newLineGraph = new SubwayGraph(line);
        newLineGraph.createNewLine(upLineStation, downLineStation, distance);
        final List<Station> allStationsInOrder = newLineGraph.findAllStationsInOrder();
        subwayGraphs.add(newLineGraph);
        return new LineDto(line, allStationsInOrder);
    }

    public int findOrderOf(Line line, Station station) {
        final SubwayGraph lineGraph = findSubwayGraphOf(line);

        return lineGraph.findOrderOf(station);
    }

    public EdgeEntity findEdge(Line line, Station station) {
        final SubwayGraph subwayGraph = findSubwayGraphOf(line);
        return subwayGraph.findEdge(station);
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

    private SubwayGraph findSubwayGraphOf(final Line line) {
        final SubwayGraph lineGraph = subwayGraphs.stream()
                .filter(s -> s.isSameLine(line))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 노선이 존재하지 않습니다."));
        return lineGraph;
    }

    public Station createStation(final Line line, final Station upLineStation, final Station downLineStation, final int distance) {
        final SubwayGraph subwayGraph = findSubwayGraphOf(line);

        return subwayGraph.addStation(upLineStation, downLineStation, distance);
    }

    public boolean isEmptyStation(Line line, Station station) {
        return findSubwayGraphOf(line).isEmptyStation(station);
    }

    public Optional<Station> findStationByName(Line line, String name) {
        final SubwayGraph subwayGraph = findSubwayGraphOf(line);

        return subwayGraph.findStationByName(name);
    }
}
