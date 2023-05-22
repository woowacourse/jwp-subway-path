package subway.domain.line;

import java.util.Collections;
import subway.domain.section.SubwayLine;

public class Line {

    private final LineName name;
    private final String color;
    private final LineExtraFare extraFare;
    private SubwayLine subwayLine;

    public Line(final String name, final String color, final int extraFare) {
        this(name, color, extraFare, new SubwayLine(Collections.emptyList()));
    }

    public Line(final String name, final String color, final int extraFare, final SubwayLine subwayLine) {
        this.name = new LineName(name);
        this.color = color;
        this.extraFare = new LineExtraFare(extraFare);
        this.subwayLine = subwayLine;
    }

    public void updateSubwayLine(final SubwayLine subwayLine) {
        this.subwayLine = subwayLine;
    }

    public LineName name() {
        return name;
    }

    public String color() {
        return color;
    }

    public SubwayLine subwayLine() {
        return subwayLine;
    }

    public LineExtraFare extraFare() {
        return extraFare;
    }
}
