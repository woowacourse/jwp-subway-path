package subway.domain;

import subway.exception.DuplicateLineNameException;

import java.util.ArrayList;
import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public void addNewLine(Line newLine) {
        validateLineDuplicate(newLine);
        lines.add(newLine);
    }

    private void validateLineDuplicate(Line line) {
        if (hasLine(line)) {
            throw new DuplicateLineNameException("이미 존재하는 노선입니다");
        }
    }

    public ShortestPath findShortestPath() {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station("잠실"));
        stations.add(new Station("선릉"));
        stations.add(new Station("잠실나루"));
        return new ShortestPath(stations);
    }

    private boolean hasLine(Line newLine) {
        return lines.stream()
                .anyMatch(line -> line.getName().equals(newLine.getName()));
    }

    public List<Line> getLines() {
        return new ArrayList<>(lines);
    }

    @Override
    public String toString() {
        return "Lines{" +
                "lines=" + lines +
                '}';
    }
}
