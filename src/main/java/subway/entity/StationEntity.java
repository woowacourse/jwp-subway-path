package subway.entity;

import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.domain.station.Station;

@Getter
@RequiredArgsConstructor
public class StationEntity {

    private final Long id;
    private final String name;

    public StationEntity(final String name) {
        this(null, name);
    }

    public static StationEntity from(final Station station) {
        return new StationEntity(station.getName().getValue());
    }

    public Station toStation() {
        return new Station(id, name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StationEntity stationEntity = (StationEntity) o;
        return id.equals(stationEntity.id) && name.equals(stationEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
