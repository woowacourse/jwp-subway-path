package subway.entity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;

public class StationEntity {

    private final Long id;
    private final String name;

    public StationEntity(String name) {
        this(null, name);
    }

    public StationEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<StationEntity> of(Line line) {
        return line.getStations().stream()
                .map(station -> new StationEntity(station.getName()))
                .collect(Collectors.toList());
    }

    public Station toDomain() {
        return new Station(this.id, this.name);
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
        final StationEntity station = (StationEntity) o;
        return Objects.equals(id, station.id);
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
