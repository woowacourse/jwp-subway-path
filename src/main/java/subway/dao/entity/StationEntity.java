package subway.dao.entity;

import java.util.Objects;

public class StationEntity {
    private final Long id;
    private final String name;
    private final Long lineId;

    public StationEntity(final Long id, final String name, final Long lineId) {
        this.id = id;
        this.name = name;
        this.lineId = lineId;
    }

    public StationEntity(final String name, final Long lineId) {
        this(null, name, lineId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationEntity that = (StationEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(lineId, that.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lineId);
    }
}
