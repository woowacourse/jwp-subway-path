package subway.fixture;

import static subway.fixture.LineFixture.이호선_엔티티;
import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.남위례역;
import static subway.fixture.StationFixture.복정역;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.선릉역_엔티티;
import static subway.fixture.StationFixture.신림역;
import static subway.fixture.StationFixture.잠실역;
import static subway.fixture.StationFixture.잠실역_엔티티;

import java.util.List;
import subway.domain.section.Section;
import subway.domain.section.SectionDistance;
import subway.domain.section.SubwayLine;
import subway.domain.section.dto.SectionSaveReq;

public final class SectionFixture {

    public static final Section 잠실_선릉 = new Section(잠실역, 선릉역, 10);

    public static final SectionSaveReq 구간_저장_요청 = new SectionSaveReq(이호선_엔티티.getId(), 잠실역_엔티티.getId(), 선릉역_엔티티.getId(),
        10);

    public static SubwayLine 잠실_신림_구간_정보() {
        final Section 잠실_선릉 = new Section(잠실역, 선릉역, SectionDistance.create(10));
        final Section 선릉_강남 = new Section(선릉역, 강남역, SectionDistance.create(7));
        final Section 강남_복정 = new Section(강남역, 복정역, SectionDistance.create(5));
        final Section 복정_신림 = new Section(복정역, 신림역, SectionDistance.create(3));
        final List<Section> sections = List.of(잠실_선릉, 선릉_강남, 강남_복정, 복정_신림);
        return new SubwayLine(sections);
    }

    public static SubwayLine 잠실_신림_환승_구간_정보() {
        final Section 선릉_남위례 = new Section(선릉역, 남위례역, SectionDistance.create(8));
        final Section 남위례_신림 = new Section(남위례역, 신림역, SectionDistance.create(2));
        final List<Section> sections = List.of(선릉_남위례, 남위례_신림);
        return new SubwayLine(sections);
    }
}
