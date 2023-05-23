package subway.domain.vo;

public class LineSection {

    private final Line line;

    private final Section section;

    public LineSection(final Line line, final Section section) {
        this.line = line;
        this.section = section;
    }

    public Line getLineInfo() {
        return line;
    }

    public Section getSection() {
        return section;
    }
}
