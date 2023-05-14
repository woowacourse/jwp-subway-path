package subway.fixture;

import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.fixture.LineFixture.이호선;
import subway.fixture.StationFixture.건대역;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@SuppressWarnings("NonAsciiCharacters")
public class SectionFixture {

    public static class 이호선_역삼_삼성_3 {

        private static final Long ID = 1L;
        private static final LineEntity LINE_ENTITY = 이호선.ENTITY;
        private static final Station UP_STATION = 역삼역.STATION;
        private static final StationEntity UP_STATION_ENTITY = 역삼역.ENTITY;
        private static final Station DOWN_STATION = 삼성역.STATION;
        private static final StationEntity DOWN_STATION_ENTITY = 삼성역.ENTITY;
        private static final int DISTANCE = 3;

        public static final Section SECTION = new Section(ID, UP_STATION, DOWN_STATION, DISTANCE);
        public static final SectionEntity ENTITY = new SectionEntity(ID, LINE_ENTITY.getId(), UP_STATION_ENTITY.getId(),
                DOWN_STATION_ENTITY.getId(), DISTANCE);
    }

    public static class 이호선_삼성_잠실_2 {

        private static final Long ID = 2L;
        private static final LineEntity LINE_ENTITY = 이호선.ENTITY;
        private static final Station UP_STATION = 삼성역.STATION;
        private static final StationEntity UP_STATION_ENTITY = 삼성역.ENTITY;
        private static final Station DOWN_STATION = 잠실역.STATION;
        private static final StationEntity DOWN_STATION_ENTITY = 잠실역.ENTITY;
        private static final int DISTANCE = 2;

        public static final Section SECTION = new Section(ID, UP_STATION, DOWN_STATION, DISTANCE);
        public static final SectionEntity ENTITY = new SectionEntity(ID, LINE_ENTITY.getId(), UP_STATION_ENTITY.getId(),
                DOWN_STATION_ENTITY.getId(), DISTANCE);
    }

    public static class 이호선_잠실_건대_1 {

        private static final Long ID = 2L;
        private static final LineEntity LINE_ENTITY = 이호선.ENTITY;
        private static final Station UP_STATION = 잠실역.STATION;
        private static final StationEntity UP_STATION_ENTITY = 잠실역.ENTITY;
        private static final Station DOWN_STATION = 건대역.STATION;
        private static final StationEntity DOWN_STATION_ENTITY = 건대역.ENTITY;
        private static final int DISTANCE = 1;

        public static final Section SECTION = new Section(ID, UP_STATION, DOWN_STATION, DISTANCE);
        public static final SectionEntity ENTITY = new SectionEntity(ID, LINE_ENTITY.getId(), UP_STATION_ENTITY.getId(),
                DOWN_STATION_ENTITY.getId(), DISTANCE);
    }
}
