package subway.fixture;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;

import java.util.List;

public abstract class LineFixture {

    public static Line 노선(final String name, final String color) {
        return new Line(name, color, new Sections());
    }

    public static Line 노선(final String name, final String color, final List<Section> sections) {
        return new Line(name, color, Sections.from(sections));
    }

    public static Line 노선(final String name, final String color, final Sections sections) {
        return new Line(name, color, sections);
    }
}
