package subway.domain;

import java.util.ArrayList;
import java.util.List;


public class Lines {

    private final List<Line> lines = new ArrayList<>();

    public Line addNewLine(String lineName, Station station1, Station station2, int distance) {
        System.out.println("Lines.addNewLine");
        // TODO : lineName이 이미 존재하는지 검증 (중복 검증)
        Line line = Line.createLine(lineName, station1, station2, distance);
        lines.add(line);
        return line;
    }

    public Line addStationToLine(String lineName, Station upStation, Station downStation, int distance) {
        Line findLine = lines.stream()
                .filter(line -> line.getName().equals(lineName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 호선이 없습니다."));
        findLine.addEdge(upStation, downStation, distance);
        return findLine;
    }

    public List<Station> findAllStation(String lineName) {
        Line line = lines.stream()
                .filter(it -> it.getName().equals(lineName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 호선이 없습니다."));
        return line.getStations();

    }
}
