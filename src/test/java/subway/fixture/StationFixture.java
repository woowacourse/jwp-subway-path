package subway.fixture;

import subway.domain.Station;

public abstract class StationFixture {

    public static Station 역(final String 역명) {
        return new Station(역명);
    }
}
