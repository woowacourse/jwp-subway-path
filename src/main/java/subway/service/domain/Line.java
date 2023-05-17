package subway.service.domain;

import java.util.List;

public class Line {

    private final LineProperty lineProperty;
    private final Sections sections;

    public Line(LineProperty lineProperty, Sections sections) {
        this.lineProperty = lineProperty;
        this.sections = sections;
    }

    public List<Section> findSectionByStation(Station station) {
        return sections.findContainsThisStation(station);
    }

    public LineProperty getLineProperty() {
        return lineProperty;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    @Override
    public String toString() {
        return "Line{" +
                "lineProperty=" + lineProperty +
                ", sections=" + sections +
                '}';
    }
}
