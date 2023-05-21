package subway.entity;

import java.util.Objects;

public class StationEntity {

    private Long id;
    private String name;

    private StationEntity() {
    }

    public StationEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public StationEntity(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationEntity stationEntity = (StationEntity) o;
        return Objects.equals(id, stationEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StationEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
