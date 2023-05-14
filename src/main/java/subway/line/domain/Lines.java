package subway.line.domain;

import subway.line.exception.DuplicateLineNameException;

import java.util.ArrayList;
import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public List<Line> getLines() {
        return new ArrayList<>(lines);
    }

    public void addLine(Line lineToAdd) {
        if (isLineNameExists(lineToAdd)) {
            throw new DuplicateLineNameException("이미 존재하는 노선 이름입니다");
        }
    }

    private boolean isLineNameExists(Line lineToAdd) {
        return lines.stream()
                    .map(Line::getName)
                    .anyMatch(lineName -> lineName.equals(lineToAdd.getName()));
    }
}
