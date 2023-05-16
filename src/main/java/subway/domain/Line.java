package subway.domain;

import java.util.*;

public class Line {

    private final Long id;
    private final LineName name;
    private final LineColor color;
    private final Sections sections;

    public Line(final LineName name, final LineColor color) {
        this(null, name, color, new Sections(new ArrayList<>()));
    }

    public Line(final LineName name, final LineColor color, final Sections sections) {
        this(null, name, color, sections);
    }

    public Line(final Long id, final LineName name, final LineColor color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line addSection(final Section upSection, final Section downSection) {
        if (upSection.equals(Section.EMPTY_SECTION) && downSection.equals(Section.EMPTY_SECTION)) {
            throw new IllegalArgumentException("추가할 구간이 존재하지 않습니다.");
        }
        if (upSection.equals(Section.EMPTY_SECTION)) {
            return new Line(id, name, color, sections.addFirstSection(downSection));
        }
        if (downSection.equals(Section.EMPTY_SECTION)) {
            return new Line(id, name, color, sections.addLastSection(upSection));
        }
        return new Line(id, name, color, sections.addMiddleSection(upSection, downSection));
    }

    // TODO: 5/16/23 Section이 가지고 있는 분기 좀 나눠 갖기
    public Line removeStation(final Station station) {
        return new Line(id, name, color, sections.removeStation(station));
    }

    public Line addInitSection(final Section newSection) {
        return new Line(id, name, color, sections.addInitSection(newSection));
    }

    public List<Station> findAllStation() {
        return sections.findAllStationUpToDown();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getColor() {
        return color.getValue();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public Station getFirstStation() {
        return sections.getFirstStation();
    }

    public Station getLastStation() {
        return sections.getLastStation();
    }
}
