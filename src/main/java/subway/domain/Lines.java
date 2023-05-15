package subway.domain;

import java.util.List;


public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Line addNewLine(String lineName, Station station1, Station station2, int distance) {
        validateLineName(lineName);
        Line line = Line.createLine(lineName, station1, station2, distance);
        lines.add(line);
        return line;
    }

    private void validateLineName(String lineName) {
        lines.stream()
                .filter(line -> line.getName().equals(lineName))
                .findAny()
                .ifPresent(line -> new IllegalArgumentException("이미 존재하는 노선입니다."));
    }

    public Line addStationToLine(Line line, Station upStation, Station downStation, int distance) {
        line.addEdge(upStation, downStation, distance);
        return line;
    }

    public List<Station> findAllStation(Line line) {
        return line.getStations();
    }

    public Line deleteStationFromLine(Line line, Station station) {
        line.deleteStation(station);
        // TODO : line안에 edge없으면 line 삭제
        return line;
    }
}
