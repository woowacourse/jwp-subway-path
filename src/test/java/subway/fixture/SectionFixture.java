package subway.fixture;

import static subway.fixture.LineFixture.이호선_엔티티;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.선릉역_엔티티;
import static subway.fixture.StationFixture.잠실역;
import static subway.fixture.StationFixture.잠실역_엔티티;

import subway.domain.section.Section;
import subway.domain.section.dto.SectionSaveReq;

public final class SectionFixture {

    public static final Section 잠실_선릉 = new Section(잠실역, 선릉역, 10);

    public static final SectionSaveReq 구간_저장_요청 = new SectionSaveReq(이호선_엔티티.getId(), 잠실역_엔티티.getId(), 선릉역_엔티티.getId(),
        10);
}
