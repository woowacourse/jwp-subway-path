package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Line {
    private Long id;
    private String name;
    private String color;

    private List<Section> sections;


    public Line() {
    }

    public Line(String name, String color, Section section) {
        validateSection(section);
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>();
        sections.add(section);
    }

    private void validateSection(Section section) {

        if (section.isIncludeEmptyStation()) {
            throw new IllegalArgumentException("상행역 혹은 하행역을 입력하지 않았습니다.");
        }
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
