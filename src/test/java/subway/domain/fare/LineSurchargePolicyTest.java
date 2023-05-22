package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.route.Route;
import subway.domain.route.RouteSection;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineSurchargePolicyTest {

    private LineSurchargePolicy lineSurchargePolicy;

    @BeforeEach
    void setUp() {
        lineSurchargePolicy = new LineSurchargePolicy();
    }

    @Test
    void 가장_비싼_추가요금을_반환한다() {
        // given
        final Section section1 = new Section(1L, 역삼역.STATION, 삼성역.STATION, 10);
        final Section section2 = new Section(2L, 삼성역.STATION, 잠실역.STATION, 10);
        final Line line1 = new Line(1L, "이호선", "GREEN", 100, List.of(section1));
        final Line line2 = new Line(2L, "삼호선", "ORANGE", 200, List.of(section2));

        Route route = new Route(List.of(new RouteSection(line1, section1), new RouteSection(line2, section2)));

        // when
        Fare fare = lineSurchargePolicy.calculate(route);

        // then
        assertThat(fare).isEqualTo(new Fare(200));
    }
}
