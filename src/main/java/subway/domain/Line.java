package subway.domain;

import java.util.Objects;
import subway.domain.exception.BusinessException;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;

    public Line(final Long id, final String name, final String color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(final String name, final String color) {
        this(null, name, color, null);
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, null);
    }

    public Line(final String name, final String color, final Sections sections) {
        this(null, name, color, sections);
    }

    public void addTopStation(final Station station) {
        if (sections.isEmpty()) {
            throw new BusinessException("호선에 최소 2개의 역이 필요합니다.");
        }
        final Section currentTopSection = sections.getFirstSection();
        final Station currentTopStation = currentTopSection.getUpStation();
        final Section section = new Section(station, currentTopStation);
        sections.addTop(section);
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
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    public int getStationsSize() {
        return sections.getStationsSize();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
