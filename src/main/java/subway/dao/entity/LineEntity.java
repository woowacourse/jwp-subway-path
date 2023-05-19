package subway.dao.entity;

import java.util.Objects;

public class LineEntity {
    private final Long id;
    private final String name;

    public LineEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public LineEntity(final String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineEntity that = (LineEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}