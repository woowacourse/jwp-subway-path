package subway.domain.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.fixture.LineFixture.삼호선;
import subway.fixture.LineFixture.이호선;
import subway.fixture.SectionFixture.삼호선_잠실_고터_2;
import subway.fixture.SectionFixture.이호선_삼성_잠실_2;
import subway.fixture.SectionFixture.이호선_역삼_삼성_3;
import subway.fixture.SectionFixture.이호선_잠실_건대_1;
import subway.fixture.StationFixture.건대역;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class RouteTest {

    @Test
    void 경로가_null일때_예외() {
        // given
        List<RouteSection> routeSections = null;

        // when then
        assertThatThrownBy(() -> new Route(routeSections))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("경로에는 1개 이상의 구간이 포함되어야합니다.");
    }

    @Test
    void 경로에_구간이_1개보다_적으면_예외() {
        // when then
        assertThatThrownBy(() -> new Route(Collections.emptyList()))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("경로에는 1개 이상의 구간이 포함되어야합니다.");
    }

    @Test
    void 경로가_연속되지_않으면_예외() {
        // given
        Section section1 = 이호선_역삼_삼성_3.SECTION;
        Section section2 = 이호선_잠실_건대_1.SECTION;
        Line line = new Line(1L, "2호선", "GREEN", 0, List.of(section1, section2));

        List<RouteSection> routeSections = List.of(
                new RouteSection(line, section1),
                new RouteSection(line, section2)
        );

        // when then
        assertThatThrownBy(() -> new Route(routeSections))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("경로의 구간은 연속되어야합니다.");
    }

    @Test
    void 경로_생성() {
        Section section1 = 이호선_역삼_삼성_3.SECTION;
        Line line1 = new Line(1L, "2호선", "GREEN", 0, List.of(section1));

        List<RouteSection> routeSections = List.of(
                new RouteSection(line1, section1)
        );

        // when then
        assertThatNoException().isThrownBy(() -> new Route(routeSections));
    }

    @Test
    void 역_경로_조회() {
        // given
        Section section1 = 이호선_역삼_삼성_3.SECTION;
        Section section2 = 이호선_삼성_잠실_2.SECTION;
        Section section3 = 이호선_잠실_건대_1.SECTION;
        Line line = new Line(1L, "2호선", "GREEN", 0, List.of(section1, section2, section3));

        List<RouteSection> routeSections = List.of(
                new RouteSection(line, section1),
                new RouteSection(line, section2),
                new RouteSection(line, section3)
        );
        Route route = new Route(routeSections);

        // when
        List<Station> stationRoute = route.findStationRoute();

        // then
        assertThat(stationRoute).isEqualTo(List.of(역삼역.STATION, 삼성역.STATION, 잠실역.STATION, 건대역.STATION));
    }

    @Test
    void 지나온_노선_조회() {
        // given
        Section section1 = 이호선_역삼_삼성_3.SECTION;
        Section section2 = 이호선_삼성_잠실_2.SECTION;
        Section section3 = 삼호선_잠실_고터_2.SECTION;
        Line line1 = new Line(1L, "2호선", "GREEN", 0, List.of(section1, section2));
        Line line2 = new Line(2L, "3호선", "ORANGE", 0, List.of(section3));

        List<RouteSection> routeSections = List.of(
                new RouteSection(line1, section1),
                new RouteSection(line1, section2),
                new RouteSection(line2, section3)
        );
        Route route = new Route(routeSections);

        // when
        Set<Line> lines = route.findLines();

        // then
        assertThat(lines).containsExactly(이호선.LINE, 삼호선.LINE);
    }

    @Test
    void 총_비용_조회() {
        // given
        Section section1 = 이호선_역삼_삼성_3.SECTION;
        Section section2 = 이호선_삼성_잠실_2.SECTION;
        Section section3 = 삼호선_잠실_고터_2.SECTION;
        Line line1 = new Line(1L, "2호선", "GREEN", 0, List.of(section1, section2));
        Line line2 = new Line(2L, "3호선", "ORANGE", 0, List.of(section3));

        List<RouteSection> routeSections = List.of(
                new RouteSection(line1, section1),
                new RouteSection(line1, section2),
                new RouteSection(line2, section3)
        );
        Route route = new Route(routeSections);

        // when
        int totalDistance = route.findTotalDistance();

        // then
        assertThat(totalDistance).isEqualTo(7);
    }
}
