package subway.fixture;

import subway.domain.Station;

public class StationFixture {

    public static class JamsilStation {
        private static final String name = "잠실역";

        public static final Station jamsilStation = Station.of(name);
    }

    public static class GangnamStation {
        private static final String name = "강남역";

        public static final Station gangnamStation = Station.of(name);
    }
}
