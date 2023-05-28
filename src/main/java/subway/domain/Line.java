package subway.domain;

import subway.domain.vo.Color;
import subway.domain.vo.Name;

import java.util.List;
import java.util.Objects;

public class Line {

    private static final int NAME_LENGTH = 20;

    private final Long id;
    private final Name name;
    private final Color color;
    private final Sections sections;

    public Line(final String name, final String color, final Sections sections) {
        this(null, name, color, sections);
    }

    public Line(final Long id, final String name, final String color, final Sections sections) {
        validate(name, color, sections);
        this.id = id;
        this.name = Name.from(name);
        this.color = new Color(color);
        this.sections = sections;
    }

    private void validate(final String name, final String color, final Sections sections) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("노선 이름을 null일 수 없습니다.");
        }
        if (name.isBlank() || name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException("노선 이름의 길이는 1이상 20이하이어야 합니다.");
        }
        if (Objects.isNull(color)) {
            throw new IllegalArgumentException("노선 색상은 null일 수 없습니다.");
        }
        if (Objects.isNull(sections)) {
            throw new IllegalArgumentException("노선의 구간 목록은 null일 수 없습니다.");
        }
    }

    public void addSection(final Section section) {
        sections.addSection(section);
    }

    public void removeSection(final Station station) {
        sections.removeStation(station);
    }

    public List<Station> getAllStations() {
        return sections.collectAllStations();
    }

    public Long getId() {
        return id;
    }

    public String getNameValue() {
        return name.getValue();
    }

    public String getColorValue() {
        return color.getValue();
    }

    public List<Section> getSections() {
        return sections.getOldSections();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, sections);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sections=" + sections +
                '}';
    }
}
