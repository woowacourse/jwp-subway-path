package subway.domain.line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.fare.Fare;

public class Line {

    private final Long id;
    private final LineInfo lineInfo;
    private final Sections sections;

    public Line(final String name, final String color, final Integer surcharge) {
        this(null, name, color, surcharge, Collections.emptyList());
    }

    public Line(final Long id, final String name, final String color, final Integer surcharge,
            final List<Section> sections) {
        this.id = id;
        this.lineInfo = new LineInfo(name, color, surcharge);
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

    public boolean containsSection(final Section section) {
        return sections.contains(section);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Line line = (Line) o;

        return id != null ? id.equals(line.id) : line.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
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

    public Fare getSurcharge() {
        return lineInfo.getSurcharge();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }
}
