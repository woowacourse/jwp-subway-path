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

class FareStrategyCompositeTest {

    @Test
    @DisplayName("지정된 요금 정책을 수행 후 요금을 확인한다.")
    void calculateFare() {
        final FareStrategyComposite fareStrategyComposite = new FareStrategyComposite();
        final Line lineOfTwo = new Line(2L, "2호선", "초록색", 1000);
        lineOfTwo.addSection(GANGNAM, YANGJAE, 12);
        final Subway subway = new Subway(new SubwayJgraphtGraph(List.of(lineOfTwo)));
        final Passenger passenger = new Passenger(15, GANGNAM, YANGJAE);

        final double result = fareStrategyComposite.calculateFare(0, passenger, subway);

        assertThat(result).isEqualTo(1600);
    }
}
