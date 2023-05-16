package subway.domain.lineDetail.entity;

import java.util.Objects;

public class LineDetailEntity {

    private Long id;
    private String name;
    private String color;

    private LineDetailEntity() {
    }

    public LineDetailEntity(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineDetailEntity(final String name, final String color) {
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
        LineDetailEntity lineDetailEntity = (LineDetailEntity) o;
        return Objects.equals(id, lineDetailEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
