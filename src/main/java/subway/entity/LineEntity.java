package subway.entity;

import java.util.Objects;

public class LineEntity {

    private Long id;
    private String name;
    private String color;

    private LineEntity() {
    }

    public LineEntity(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineEntity(final String name, final String color) {
        this.name = name;
        this.color = color;
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineEntity lineEntity = (LineEntity) o;
        return Objects.equals(id, lineEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
