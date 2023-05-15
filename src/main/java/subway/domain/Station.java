package subway.domain;

import java.util.Objects;
import subway.dao.entity.StationEntity;

public class Station {
    private final String name;

    public Station(String name) {
        this.name = name;
    }

    public static Station fromEntity(StationEntity stationEntity) {
        return new Station(stationEntity.getName());
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Station)) {
            return false;
        }

        Station station = (Station) o;

        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
