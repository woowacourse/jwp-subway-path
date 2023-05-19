package subway.fixture;

import subway.station.domain.entity.StationEntity;
import subway.vo.Name;

public class StationFixture {

    public static class JamsilStation {
        private static final String name = "잠실역";

        public static final StationEntity JAMSIL_STATION_ENTITY = StationEntity.of(Name.from(name));
    }

    public static class GangnamStation {
        private static final String name = "강남역";

        public static final StationEntity GANGNAM_STATION_ENTITY = StationEntity.of(Name.from(name));
    }
}
