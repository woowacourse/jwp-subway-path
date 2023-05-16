package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Line {

    private final Long id;
    private final LineInfo lineInfo;
    private final Sections sections;

    public Line(final String name, final String color) {
        this(null, name, color, Collections.emptyList());
    }

    public Line(final Long id, final String name, final String color, final List<Section> sections) {
        this.id = id;
        this.lineInfo = new LineInfo(name, color);
        this.sections = new Sections(new ArrayList<>(sections));
    }

    public void addSection(final Section section) {
        sections.add(section);
    }

    public void removeStation(final Station station) {
        sections.remove(station);
    }

    public List<Station> findOrderedStation() {
        return sections.findOrderedStation();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return lineInfo.getName();
    }

    public String getColor() {
        return lineInfo.getColor();
    }
    
    public List<Section> getSections() {
        return sections.getSections();
    }
}
