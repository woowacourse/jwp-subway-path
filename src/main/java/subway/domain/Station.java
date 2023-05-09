package subway.domain;

import java.util.List;

public class Station {
    private final String name;
    private final List<Line> lines;

    public Station(final String name, final List<Line> lines) {
        this.name = name;
        this.lines = lines;
    }
}
