package subway.domain.fare;

import static fixtures.StationFixtures.GANGNAM;
import static fixtures.StationFixtures.GYODAE;
import static fixtures.StationFixtures.NAMBU;
import static fixtures.StationFixtures.YANGJAE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;
import subway.domain.subway.SubwayJgraphtGraph;

class RouteFareStrategyTest {

    private static final RouteFareStrategy routeFareStrategy = new RouteFareStrategy();

    @Test
    @DisplayName("900원 추가 요금이 있는 노선을 이용하면 기존 요금에 900원이 추가된다.")
    void addition900() {
        final Line lineOfTwo = new Line(2L, "2호선", "초록색", 900);
        lineOfTwo.addSection(GANGNAM, YANGJAE, 8);
        final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
        final Passenger passenger = new Passenger(26, GANGNAM, YANGJAE);

        final double result = routeFareStrategy.calculateFare(1250, passenger, subway);

        assertThat(result).isEqualTo(2150);
    }

    @Test
    @DisplayName("0, 500, 900원의 추가 요금이 있는 노선을 경유하면 900원이 추가된다.")
    void additionHighest900() {
        final Line lineOfOne = new Line(1L, "1호선", "빨간색", 0);
        final Line lineOfTwo = new Line(2L, "2호선", "노란색", 500);
        final Line lineOfThree = new Line(3L, "3호선", "파란색", 900);
        lineOfOne.addSection(GANGNAM, YANGJAE, 3);
        lineOfTwo.addSection(YANGJAE, GYODAE, 3);
        lineOfThree.addSection(GYODAE, NAMBU, 2);
        final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfOne, lineOfTwo, lineOfThree)));
        final Passenger passenger = new Passenger(26, GANGNAM, NAMBU);

        final double result = routeFareStrategy.calculateFare(1250, passenger, subway);

        assertThat(result).isEqualTo(2150);
    }
}
