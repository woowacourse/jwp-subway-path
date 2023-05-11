package subway.domain;

import subway.dto.LineDto;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SubwayGraphs {
    private final List<SubwayGraph> subwayGraphs;

    public SubwayGraphs(final List<SubwayGraph> subwayGraphs) {
        this.subwayGraphs = subwayGraphs;
    }

    public SubwayGraphs() {
        this(new ArrayList<>());
    }

    public LineDto createLine(Line line, Station upLineStation, Station downLineStation, int distance) {
        final SubwayGraph newLineGraph = new SubwayGraph(line);
        newLineGraph.createNewLine(upLineStation, downLineStation, distance);
        final List<Station> allStationsInOrder = newLineGraph.findAllStationsInOrder();
        subwayGraphs.add(newLineGraph);
        return new LineDto(line, allStationsInOrder);
    }

    public LineDto addStation(Line line, Station upLineStation, Station downLineStation, int distance) {
        final SubwayGraph lineGraph = subwayGraphs.stream()
                .filter(s -> s.isSameLine(line))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 노선이 존재하지 않습니다."));

        lineGraph.addStation(upLineStation, downLineStation, distance);
        final List<Station> allStationsInOrder = lineGraph.findAllStationsInOrder();
        return new LineDto(line, allStationsInOrder);
    }
}
