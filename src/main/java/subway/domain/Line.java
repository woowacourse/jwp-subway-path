package subway.domain;

public class Line {
    private final LineInfo lineInfo;
    private final Sections sections;

    public Line(LineInfo lineInfo, Sections sections) {
        this.lineInfo = lineInfo;
        this.sections = sections;
    }

    public LineInfo getLineInfo() {
        return lineInfo;
    }

    public Sections getSections() {
        return sections;
    }
}
