package subway.domain;

import java.util.Map;

public class Lines {

    private final Map<Long, Line> lines;

    public Lines(Map<Long, Line> lines) {
        this.lines = lines;
    }

    public Map<Long, Line> getLines() {
        return lines;
    }
}
