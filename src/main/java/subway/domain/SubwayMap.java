package subway.domain;

import java.util.List;

public class SubwayMap {
    List<Line> lines;

    SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public void addLine(Line line) {
        validateSameName(line);
        lines.add(line);
    }

    private void validateSameName(Line newLine) {
        if (lines.stream().anyMatch((line) -> line.hasSameName(newLine))) {
            throw new IllegalArgumentException("같은 이름의 노선을 추가할 수 없습니다.");
        }
    }

    public void removeLine(String name) {
        Line line = findLineByName(name);
        lines.remove(line);
    }

    private Line findLineByName(String name) {
        return lines.stream()
                .filter(line -> line.isSameName(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("노선도에 존재하지 않는 노선입니다"));
    }
}
