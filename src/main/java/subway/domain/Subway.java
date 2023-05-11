package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Subway {

    private final List<Line> lines;

    public Subway() {
        this(new ArrayList<>());
    }

    private Subway(List<Line> lines) {
        this.lines = lines;
    }

    public void addLine(Line line) {
        validateDuplicatedLineName(line);
        lines.add(line);
    }

    private void validateDuplicatedLineName(Line line) {
        if (isDuplicatedName(line)) {
            throw new IllegalArgumentException("중복되는 이름의 노선이 이미 존재합니다.");
        }
    }

    private boolean isDuplicatedName(Line other) {
        return lines.stream().anyMatch(it -> it.isSameName(other));
    }

    public List<Line> getLines() {
        return lines;
    }

    public void removeStation(String lineName, Station station) {
        Line line = findLineByName(lineName);
        line.removeStation(station);
        if (line.isEmpty()) {
            lines.remove(line);
        }
    }

    public Line findLineByName(String lineName) {
        return lines.stream()
                .filter(line -> line.getName().equals(lineName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }
}
