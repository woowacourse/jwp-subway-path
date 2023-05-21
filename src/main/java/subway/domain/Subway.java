package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Subway {

    private final List<Line> lines;

    public Subway(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    private void validateDuplicatedName(String name) {
    }

    private void validateDuplicatedColor(String color) {
    }

    public void addLine(Line newLine) {
    }

    public void removeLine(Line line) {
    }

    public void updateLineName(Long lineId, String newName) {
    }

    public void updateLineColor(Long lineId, String newColor) {
    }

    public Line findLineById(Long id) {
        return null;
    }

    public Line findLineByName(String name) {
        return null;
    }

    public List<Station> findShortestRoute(Station start, Station end) {
        return null;
    }

    public Distance findShortestDistance(Station start, Station end) {
        return null;
    }

    public List<Line> getLines() {
        return null;
    }
}
