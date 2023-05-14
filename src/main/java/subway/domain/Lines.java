package subway.domain;

import java.util.ArrayList;
import java.util.List;


public class Lines {

    private final List<Line> lines = new ArrayList<>();

    public Line addNewLine(String lineName, Station station1, Station station2, int distance) {
        Line line = Line.createLine(lineName, station1, station2, distance);
        lines.add(line);
        return line;
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
        return line;
    }
}