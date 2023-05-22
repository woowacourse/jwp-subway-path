package subway.domain.fare;

import static fixtures.StationFixtures.GANGNAM;
import static fixtures.StationFixtures.YANGJAE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;
import subway.domain.subway.SubwayJgraphtGraph;

class AgeFareStrategyTest {

    private static final AgeFareStrategy ageFareStrategy = new AgeFareStrategy();

    @Test
    @DisplayName("이전 요금이 0이라면 요금은 없다.")
    void zero() {
        final Line lineOfTwo = new Line(2L, "2호선", "초록색", 0);
        lineOfTwo.addSection(GANGNAM, YANGJAE, 8);
        final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
        final Passenger passenger = new Passenger(5, GANGNAM, YANGJAE);

        final double result = ageFareStrategy.calculateFare(0, passenger, subway);

        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("6세 미만 어린이인 경우 할인이 적용되지 않는다.")
    void baby() {
        final Line lineOfTwo = new Line(2L, "2호선", "초록색", 0);
        lineOfTwo.addSection(GANGNAM, YANGJAE, 8);
        final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
        final Passenger passenger = new Passenger(5, GANGNAM, YANGJAE);

        final double result = ageFareStrategy.calculateFare(1350, passenger, subway);

        assertThat(result).isEqualTo(1350);
    }

    @Test
    @DisplayName("6세 이상, 13세 미만의 어린이인 경우 350원 공제 후, 50% 할인이 적용된다.")
    void kid() {
        final Line lineOfTwo = new Line(2L, "2호선", "초록색", 0);
        lineOfTwo.addSection(GANGNAM, YANGJAE, 8);
        final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
        final Passenger passenger = new Passenger(8, GANGNAM, YANGJAE);

        final double result = ageFareStrategy.calculateFare(1350, passenger, subway);

        assertThat(result).isEqualTo(500);
    }

    @Test
    @DisplayName("13세 이상, 19세 미만 청소년인 경우 350원 공제 후, 20% 할인이 적용된다.")
    void teen() {
        final Line lineOfTwo = new Line(2L, "2호선", "초록색", 0);
        lineOfTwo.addSection(GANGNAM, YANGJAE, 8);
        final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
        final Passenger passenger = new Passenger(15, GANGNAM, YANGJAE);

        final double result = ageFareStrategy.calculateFare(1350, passenger, subway);

        assertThat(result).isEqualTo(800);
    }

    @Test
    @DisplayName("19세 이상 어른인 경우 할인이 적용되지 않는다.")
    void adult() {
        final Line lineOfTwo = new Line(2L, "2호선", "초록색", 0);
        lineOfTwo.addSection(GANGNAM, YANGJAE, 8);
        final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
        final Passenger passenger = new Passenger(19, GANGNAM, YANGJAE);

        final double result = ageFareStrategy.calculateFare(1350, passenger, subway);

        assertThat(result).isEqualTo(1350);
    }
}
