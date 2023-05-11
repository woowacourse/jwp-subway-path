package subway.domain;

import java.util.List;

public class Line {

    private final LineInfo lineInfo;
    private final List<Section> line;

    public Line(final LineInfo lineInfo, final List<Section> line) {
        this.lineInfo = lineInfo;
        this.line = line;
    }

    public LineInfo getLineInfo() {
        return lineInfo;
    }

    public List<Section> getLine() {
        return line;
    }
}
