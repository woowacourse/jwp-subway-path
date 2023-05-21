package subway.service.line.domain;

import subway.persistence.dao.entity.LineEntity;

import java.util.Objects;

public class Line {
    private Long id;
    private String name;
    private String color;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static Line from(LineEntity lineEntity) {
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public Long getId() {
        if (id == null) {
            throw new IllegalStateException("현재 id값이 존재 하지않습니다.");
        }
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
