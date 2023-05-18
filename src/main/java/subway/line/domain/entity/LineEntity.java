package subway.line.domain.entity;

import subway.line.domain.Line;
import subway.section.domain.Sections;
import subway.vo.Color;
import subway.vo.Name;

import java.util.Objects;

public class LineEntity {
    private Long id;
    private Name name;
    private Color color;

    private LineEntity(final Long id, final Name name, final Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineEntity of(String name, String color) {
        return new LineEntity(null, Name.from(name), Color.from(color));
    }

    public static LineEntity of(Long id, String name, String color) {
        return new LineEntity(id, Name.from(name), Color.from(color));
    }

    public Line toDomain(final Sections sections) {
        return Line.of(id, sections, name, color);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LineEntity that = (LineEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

}
