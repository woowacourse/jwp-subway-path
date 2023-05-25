package subway.persistence.entity;

import java.util.Objects;

public class LineEntity {
    private final Long id;
    private final String name;
    private final int surcharge;

    public LineEntity(String name, int surcharge) {
        this(null, name, surcharge);
    }

    public LineEntity(Long id, String name, int surcharge) {
        this.id = id;
        this.name = name;
        this.surcharge = surcharge;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSurcharge() {
        return surcharge;
    }

    @Override
    public boolean equals(Object o) {
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
