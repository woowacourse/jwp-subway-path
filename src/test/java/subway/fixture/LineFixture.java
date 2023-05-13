package subway.fixture;

import static subway.fixture.StationFixture.강남역_엔티티;
import static subway.fixture.StationFixture.남위례역_엔티티;
import static subway.fixture.StationFixture.복정역_엔티티;
import static subway.fixture.StationFixture.산성역_엔티티;
import static subway.fixture.StationFixture.선릉역_엔티티;
import static subway.fixture.StationFixture.잠실역_엔티티;

import java.util.List;
import subway.dao.entity.LineEntity;
import subway.domain.line.Line;
import subway.domain.line.dto.LineWithSectionRes;

public final class LineFixture {

    public static final LineEntity 이호선_엔티티 = new LineEntity(1L, "이호선", "bg-green-600");
    public static final Line 이호선 = new Line(이호선_엔티티.getName(), 이호선_엔티티.getColor());
    public static final LineEntity 팔호선_엔티티 = new LineEntity(2L, "팔호선", "bg-pink-600");

    public static List<LineWithSectionRes> 이호선_구간을_포함한_응답들() {
        final LineWithSectionRes 잠실_선릉 = new LineWithSectionRes(이호선_엔티티.getId(), 이호선_엔티티.getName(),
            이호선_엔티티.getColor(), 잠실역_엔티티.getId(), 잠실역_엔티티.getName(), 선릉역_엔티티.getId(), 선릉역_엔티티.getName(), 10);
        final LineWithSectionRes 선릉_강남 = new LineWithSectionRes(이호선_엔티티.getId(), 이호선_엔티티.getName(),
            이호선_엔티티.getColor(), 선릉역_엔티티.getId(), 선릉역_엔티티.getName(), 강남역_엔티티.getId(), 강남역_엔티티.getName(), 10);
        return List.of(선릉_강남, 잠실_선릉);
    }

    public static List<LineWithSectionRes> 이호선_팔호선_구간을_포함한_응답들() {
        final LineWithSectionRes 잠실_선릉 = new LineWithSectionRes(이호선_엔티티.getId(), 이호선_엔티티.getName(),
            이호선_엔티티.getColor(), 잠실역_엔티티.getId(), 잠실역_엔티티.getName(), 선릉역_엔티티.getId(), 선릉역_엔티티.getName(), 10);
        final LineWithSectionRes 선릉_강남 = new LineWithSectionRes(이호선_엔티티.getId(), 이호선_엔티티.getName(),
            이호선_엔티티.getColor(), 선릉역_엔티티.getId(), 선릉역_엔티티.getName(), 강남역_엔티티.getId(), 강남역_엔티티.getName(), 10);

        final LineWithSectionRes 복정_남위례 = new LineWithSectionRes(팔호선_엔티티.getId(), 팔호선_엔티티.getName(),
            팔호선_엔티티.getColor(), 복정역_엔티티.getId(), 복정역_엔티티.getName(), 남위례역_엔티티.getId(), 남위례역_엔티티.getName(), 10);
        final LineWithSectionRes 남위례_산성 = new LineWithSectionRes(팔호선_엔티티.getId(), 팔호선_엔티티.getName(),
            팔호선_엔티티.getColor(), 남위례역_엔티티.getId(), 남위례역_엔티티.getName(), 산성역_엔티티.getId(), 산성역_엔티티.getName(), 10);
        return List.of(선릉_강남, 잠실_선릉, 남위례_산성, 복정_남위례);
    }
}
