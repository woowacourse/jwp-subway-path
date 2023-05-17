package subway.domain.line.domain.entity;

import subway.domain.vo.Color;
import subway.domain.vo.Name;

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    public void updateInfo(final String name, final String color) {
        this.name = Name.from(name);
        this.color = Color.from(color);
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
