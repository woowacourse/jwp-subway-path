package subway.domain;

import java.util.LinkedList;
import java.util.List;

public class Line {
    private String name;
    private LinkedList<Section> sections;

    public Line(String name, List<Section> sections) {
        this.name = name;
        this.sections = new LinkedList<>(sections);
    }

    public Line(Line otherLine) {
        this(otherLine.getName(), otherLine.getSections());
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return new LinkedList<>(sections);
    }

    public List<Section> addStation(Station newStation, Station upstream, Station downstream) {
        return null;
    }
}
