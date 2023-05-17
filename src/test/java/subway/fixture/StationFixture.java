package subway.fixture;

import subway.domain.station.Station;
import subway.domain.station.StationName;

public class StationFixture {

    public static class A {
        private static final StationName name = new StationName("A");

        public static final Station stationA = new Station(name);
    }

    public static class B {
        private static final StationName name = new StationName("B");

        public static final Station stationB = new Station(name);
    }

    public static class C {
        private static final StationName name = new StationName("C");

        public static final Station stationC = new Station(name);
    }
}
