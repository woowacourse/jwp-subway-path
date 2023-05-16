package subway.adapter.station.out;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.domain.station.Station;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class StationEntity {

    private final Long id;
    private final String name;

    public static StationEntity from(final Station station) {
        return new StationEntity(station.getId(), station.getName().getValue());
    }

    public Station toStation() {
        return new Station(id, name);
    }
}
