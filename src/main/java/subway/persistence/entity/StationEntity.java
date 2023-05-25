package subway.persistence.entity;

import java.util.Objects;

public class StationEntity {
    private final Long id;
    private final Long lineId;
    private final String name;

    public StationEntity(Long lineId, String name) {
        this(null, lineId, name);
    }

    public StationEntity(Long id, Long lineId, String name) {
        this.id = id;
        this.lineId = lineId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationEntity that = (StationEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
