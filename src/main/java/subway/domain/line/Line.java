package subway.domain.line;

import java.util.Collections;
import subway.domain.section.Sections;

public class Line {

    private final LineName name;
    private final String color;
    private final LineExtraFare extraFare;
    private Sections sections;

    public Line(final String name, final String color, final int extraFare) {
        this(name, color, extraFare, new Sections(Collections.emptyList()));
    }

    public Line(final String name, final String color, final int extraFare, final Sections sections) {
        this.name = new LineName(name);
        this.color = color;
        this.extraFare = new LineExtraFare(extraFare);
        this.sections = sections;
    }

    public void updateSections(final Sections sections) {
        this.sections = sections;
    }

    public LineName getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return sections;
    }

    public LineExtraFare getExtraFare() {
        return extraFare;
    }
}
