package subway.fixture;

import subway.entity.Section;
import subway.fixture.LineFixture.이호선;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

public class SectionFixture {


    public static class 이호선_역삼_삼성 {

        public static final Section SECTION = new Section(1L, 이호선.LINE.getId(), 역삼역.STATION.getId(),
                삼성역.STATION.getId(), 3);
    }


    public static class 이호선_삼성_잠실 {

        public static final Section SECTION = new Section(2L, 이호선.LINE.getId(), 삼성역.STATION.getId(),
                잠실역.STATION.getId(), 2);
    }
}
