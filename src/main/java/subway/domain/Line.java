package subway.domain;

import java.util.ArrayList;
import java.util.List;
import subway.exception.ErrorCode;
import subway.exception.InvalidException;

public class Line {
    private final String name;
    private final Sections sections;

    private Line(final String name, final Sections sections) {
        this.name = name;
        this.sections = sections;
    }

    public static Line of(final String name, final List<Section> sections) {
        if (sections.isEmpty()) {
            return new Line(name, new Sections(sections));
        }
        return new Line(name, Sections.from(sections));
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        Section newSection = new Section(upStation, downStation, distance);
        sections.addSection(newSection);
    }

    public void deleteStation(final Station station) {
        if (sections.isEmpty()) {
            throw new InvalidException(ErrorCode.INVALID_DELETE_SECTION_REQUEST);
        }
        sections.deleteStation(station);
    }

    public String getName() {
        return name;
    }

    public List<Section> getSectionsByList() {
        return new ArrayList<>(sections.getSections());
    }

    public List<Station> getStations() {
        return new ArrayList<>(sections.getStations());
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }
}
