package subway.fixture;

import subway.entity.SectionEntity;
import subway.fixture.LineFixture.이호선;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

public class SectionFixture {


    public static class 이호선_역삼_삼성 {

        public static final SectionEntity ENTITY = new SectionEntity(1L, 이호선.ENTITY.getId(),
                역삼역.ENTITY.getId(),
                삼성역.ENTITY.getId(), 3);
    }


    public static class 이호선_삼성_잠실 {

        public static final SectionEntity ENTITY = new SectionEntity(2L, 이호선.ENTITY.getId(),
                삼성역.ENTITY.getId(),
                잠실역.STATION_ENTITY.getId(), 2);
    }
}
