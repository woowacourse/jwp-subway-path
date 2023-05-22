package subway.domain.fare;

import static fixtures.StationFixtures.GANGNAM;
import static fixtures.StationFixtures.YANGJAE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;
import subway.domain.subway.SubwayJgraphtGraph;

class DistanceFareStrategyTest {

    private static final DistanceFareStrategy distanceFareStrategy = new DistanceFareStrategy();

    @Test
    @DisplayName("거리가 10km 이내라면 요금은 기본운임 요금 1250원이다.")
    void calculateFare() {
        final Line lineOfTwo = new Line(2L, "2호선", "초록색", 500);
        lineOfTwo.addSection(GANGNAM, YANGJAE, 9);
        final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
        final Passenger passenger = new Passenger(26, GANGNAM, YANGJAE);

        final double result = distanceFareStrategy.calculateFare(0, passenger, subway);

        assertThat(result).isEqualTo(1250);
    }

    @Nested
    @DisplayName("거리가 10km~50km일 때 5km마다 100원을 추가한다면 ")
    class BaseTenDistance {

        @Test
        @DisplayName("거리가 12km라면 요금은 1350원이다.")
        void distanceTwelve() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색", 500);
            lineOfTwo.addSection(GANGNAM, YANGJAE, 12);
            final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
            final Passenger passenger = new Passenger(26, GANGNAM, YANGJAE);

            final double result = distanceFareStrategy.calculateFare(0, passenger, subway);

            assertThat(result).isEqualTo(1350);
        }

        @Test
        @DisplayName("거리가 16km라면 요금은 1450원이다.")
        void distanceSixTeen() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색", 500);
            lineOfTwo.addSection(GANGNAM, YANGJAE, 16);
            final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
            final Passenger passenger = new Passenger(26, GANGNAM, YANGJAE);

            final double result = distanceFareStrategy.calculateFare(0, passenger, subway);

            assertThat(result).isEqualTo(1450);
        }
    }

    @Nested
    @DisplayName("거리가 50km 초과할 때 8km마다 100원을 추가한다면")
    class BaseFiftyDistance {

        @Test
        @DisplayName("거리가 58km라면 요금은 2150원이다.")
        void distanceFiftyEight() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색", 500);
            lineOfTwo.addSection(GANGNAM, YANGJAE, 58);
            final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
            final Passenger passenger = new Passenger(26, GANGNAM, YANGJAE);

            final double result = distanceFareStrategy.calculateFare(0, passenger, subway);

            assertThat(result).isEqualTo(2150);
        }

        @Test
        @DisplayName("거리가 66km라면 요금은 2250원이다.")
        void distanceSixtySix() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색", 500);
            lineOfTwo.addSection(GANGNAM, YANGJAE, 66);
            final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
            final Passenger passenger = new Passenger(26, GANGNAM, YANGJAE);

            final double result = distanceFareStrategy.calculateFare(0, passenger, subway);

            assertThat(result).isEqualTo(2250);
        }
    }
}
