package subway.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.domain.Name;
import subway.domain.Station;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class StationEntity {
    private final Long id;
    private final String name;

    public static StationEntity of(long id, Station station) {
        return new StationEntity(id, station.getName().getName());
    }
    public static StationEntity of(long id, Name name) {
        return new StationEntity(id, name.getName());
    }

    public static StationEntity of(long id, String name) {
        return new StationEntity(id, name);
    }
}
