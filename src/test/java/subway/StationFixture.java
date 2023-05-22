package subway;

import subway.application.core.domain.Station;

public class StationFixture {

    public static Station of(Long id, String name) {
        return new Station(id, name);
    }

    public static Station ofNullId(String name) {
        return new Station(null, name);
    }
}
