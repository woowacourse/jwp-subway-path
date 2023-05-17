package subway.domain.line.domain;

import subway.domain.vo.Color;
import subway.domain.vo.Name;

public class Line {

    private final Name name;
    private final Color color;

    public Line(final Name name, final Color color) {
        this.name = name;
        this.color = color;
    }


}
