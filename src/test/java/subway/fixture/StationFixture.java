package subway.fixture;

import subway.domain.Station;

public abstract class StationFixture {

    public static Station 역(final String name) {
        return new Station(name);
    }
}
