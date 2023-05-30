package subway.domain;

import java.util.List;
import java.util.Objects;

public class Line {
    private Long id;
    private String name;
    private String color;

    private Sections sections;

    public Line() {
    }

    public Line(String name, String color) {
        this(null, name, color);
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, new Sections());
    }

    public Line(Long id, String name, String color, Sections sections) {
        validateName(name);
        validateColor(color);
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("노선 이름이 입력되지 않았습니다.");
        }
    }

    private void validateColor(final String color) {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("색깔이 입력되지 않았습니다.");
        }
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

    public List<Section> getSections() {
        return sections.getSections();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sections);
    }
}
