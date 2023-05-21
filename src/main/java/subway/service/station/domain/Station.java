package subway.service.station.domain;

import subway.persistence.dao.entity.StationEntity;

import java.util.Objects;

public class Station {
    private Long id;
    private String name;

    public Station() {
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        this.name = name;
    }

    public static Station from(StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public Long getId() {
        if (id == null) {
            throw new IllegalStateException("현재 id값이 존재 하지않습니다.");
        }
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSame(Station anotherStation) {
        return this.name.equals(anotherStation.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return id.equals(station.id) && name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
