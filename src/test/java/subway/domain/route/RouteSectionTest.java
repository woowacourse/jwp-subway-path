package subway.domain.route;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.Line;
import subway.domain.Section;
import subway.fixture.LineFixture.이호선;
import subway.fixture.SectionFixture.삼호선_잠실_고터_2;

class RouteSectionTest {

    @Test
    void 해당_노선의_구간이_아닌_경우_예외() {
        // given
        Line line = 이호선.LINE;
        Section section = 삼호선_잠실_고터_2.SECTION;

        // when then
        assertThatThrownBy(() -> new RouteSection(line, section))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("해당 노선의 구간이 아닙니다.");
    }
}
