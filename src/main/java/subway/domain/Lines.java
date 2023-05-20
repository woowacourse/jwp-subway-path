package subway.domain;

import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public Line findById(Long id) {
        for (Line line : lines) {
            if (line.getId() == id) {
                return line;
            }
        }

        throw new IllegalArgumentException("id에 해당하는 Line은 존재하지 않습니다");
    }

    public List<Line> getLines() {
        return lines;
    }
}
