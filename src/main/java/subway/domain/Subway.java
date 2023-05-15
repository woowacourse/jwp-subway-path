package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Subway {

    private final List<Line> lines;

    public Subway(final List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public List<Line> getLines(){
        return lines;
    }
}
