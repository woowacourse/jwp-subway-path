package subway.fixture;

import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.entity.StationEntity;

public class StationFixture {

    public static class STATION_A {
        private static final StationName name = new StationName("A");

        public static final Station stationA = new Station(1L, name);

        public static final StationEntity entity = StationEntity.from(stationA);
    }

    public static class STATION_B {
        private static final StationName name = new StationName("B");

        public static final Station stationB = new Station(2L, name);

        public static final StationEntity entity = StationEntity.from(stationB);
    }

    public static class STATION_C {
        private static final StationName name = new StationName("C");

        public static final Station stationC = new Station(3L, name);

        public static final StationEntity entity = StationEntity.from(stationC);
    }
}
