package subway.domain;

public class LineSection {

    private Line line;

    private Section section;

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
