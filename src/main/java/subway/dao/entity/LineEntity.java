package subway.dao.entity;

import subway.domain.LineDomain;
import subway.domain.SectionsDomain;

import java.util.Objects;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;

    public LineEntity(final String name, final String color) {
        this(null, name, color);
    }

    public LineEntity(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineDomain toDomain(final SectionsDomain sections) {
        return new LineDomain(id, name, color, sections);
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
        final LineEntity that = (LineEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
