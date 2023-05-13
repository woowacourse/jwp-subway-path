package subway.persistence.entity;

import java.util.Objects;

public class LineEntity {
    private final Long id;
    private final String name;
    private final String color;

    LineEntity(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineEntity(String name, String color) {
        this(null, name, color);
    }

    public static LineEntity of(Long id, LineEntity lineEntity) {
        return new LineEntity(id, lineEntity.name, lineEntity.color);
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
        LineEntity line = (LineEntity) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
