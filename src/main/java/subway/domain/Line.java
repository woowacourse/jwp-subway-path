package subway.domain;

import java.util.ArrayList;
import java.util.List;
import subway.exception.ErrorMessage;
import subway.exception.InvalidException;

public class Line {
    private final Long id;
    private final String name;
    private final Sections sections;

    private Line(final Long id, final String name, final Sections sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public static Line createWithoutId(final String name, final List<Section> sections) {
        return Line.of(null, name, sections);
    }

    public static Line of(final Long id, final String name, final List<Section> sections) {
        if (sections.isEmpty()) {
            return new Line(id, name, new Sections(sections));
        }
        return new Line(id, name, Sections.from(sections));
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        Section newSection = Section.of(upStation, downStation, distance);
        sections.addSection(newSection);
    }

    public void deleteStation(final Station station) {
        if (sections.isEmpty()) {
            throw new InvalidException(ErrorMessage.INVALID_DELETE_SECTION_REQUEST);
        }
        sections.deleteStation(station);
    }

    public List<Section> getSectionsByList() {
        return new ArrayList<>(sections.getSections());
    }

    public List<Station> getStations() {
        return new ArrayList<>(sections.getStations());
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public boolean isEmptyLine() {
        return sections.isEmpty();
    }
}
