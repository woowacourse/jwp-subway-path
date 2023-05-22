package subway.fixture;

import subway.adapter.in.web.section.dto.AddStationToLineRequest;
import subway.adapter.out.persistence.entity.LineEntity;
import subway.adapter.out.persistence.entity.SectionEntity;
import subway.adapter.out.persistence.entity.StationEntity;
import subway.domain.Section;
import subway.domain.Station;
import subway.fixture.LineFixture.삼호선;
import subway.fixture.LineFixture.이호선;
import subway.fixture.StationFixture.건대역;
import subway.fixture.StationFixture.고터역;
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
        public static final AddStationToLineRequest REQUEST = new AddStationToLineRequest(UP_STATION.getId(),
                DOWN_STATION.getId(), DISTANCE);
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
        public static final AddStationToLineRequest REQUEST = new AddStationToLineRequest(UP_STATION.getId(),
                DOWN_STATION.getId(), DISTANCE);
    }

    public static class 이호선_잠실_건대_1 {

        private static final Long ID = 3L;
        private static final LineEntity LINE_ENTITY = 이호선.ENTITY;
        private static final Station UP_STATION = 잠실역.STATION;
        private static final StationEntity UP_STATION_ENTITY = 잠실역.ENTITY;
        private static final Station DOWN_STATION = 건대역.STATION;
        private static final StationEntity DOWN_STATION_ENTITY = 건대역.ENTITY;
        private static final int DISTANCE = 1;

        public static final Section SECTION = new Section(ID, UP_STATION, DOWN_STATION, DISTANCE);
        public static final SectionEntity ENTITY = new SectionEntity(ID, LINE_ENTITY.getId(), UP_STATION_ENTITY.getId(),
                DOWN_STATION_ENTITY.getId(), DISTANCE);
        public static final AddStationToLineRequest REQUEST = new AddStationToLineRequest(UP_STATION.getId(),
                DOWN_STATION.getId(), DISTANCE);
    }

    public static class 삼호선_잠실_고터_2 {

        private static final Long ID = 4L;
        private static final LineEntity LINE_ENTITY = 삼호선.ENTITY;
        private static final Station UP_STATION = 잠실역.STATION;
        private static final StationEntity UP_STATION_ENTITY = 잠실역.ENTITY;
        private static final Station DOWN_STATION = 고터역.STATION;
        private static final StationEntity DOWN_STATION_ENTITY = 고터역.ENTITY;
        private static final int DISTANCE = 2;

        public static final Section SECTION = new Section(ID, UP_STATION, DOWN_STATION, DISTANCE);
        public static final SectionEntity ENTITY = new SectionEntity(ID, LINE_ENTITY.getId(), UP_STATION_ENTITY.getId(),
                DOWN_STATION_ENTITY.getId(), DISTANCE);
        public static final AddStationToLineRequest REQUEST = new AddStationToLineRequest(UP_STATION.getId(),
                DOWN_STATION.getId(), DISTANCE);
    }
}
