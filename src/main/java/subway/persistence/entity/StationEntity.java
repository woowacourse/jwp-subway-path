package subway.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.Station;

@Getter
@AllArgsConstructor
public class StationEntity {
    private final Long id;
    private final String name;

    public StationEntity(String name) {
        this(null, name);
    }

    public Station toStation() {
        return new Station(name);
    }
}
