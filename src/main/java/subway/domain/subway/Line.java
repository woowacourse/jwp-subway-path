package subway.domain.subway;

import subway.domain.common.Color;
import subway.domain.common.LineNumber;
import subway.domain.common.Name;

import java.util.List;

public class Line {

    private final Sections sections;
    private final LineNumber lineNumber;
    private final Name name;
    private final Color color;

    public Line(final Sections sections, final long lineNumber, final String name, final String color) {
        this.sections = sections;
        this.lineNumber = new LineNumber(lineNumber);
        this.name = new Name(name);
        this.color = new Color(color);
    }

    public long getLineNumber() {
        return lineNumber.getLineNumber();
    }

    public void addSection(final Section section) {
        this.sections.addSection(section);
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public String getName() {
        return name.getName();
    }

    public Color getColor() {
        // 추후에 사용할 것 같아서 일단 지우지 않겠습니다!
        return color;
    }
}
