package subway.domain.fare;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.Station;
import subway.domain.fare.strategy.DistanceFareStrategy;
import subway.domain.path.Path;
import subway.domain.path.PathEdgeProxy;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFareStrategyTest {

    private final DistanceFareStrategy fareStrategy = new DistanceFareStrategy();

    @CsvSource(value = {"10,1250", "25,1550", "50,2050", "60,2250", "68,2350", "106,2750", "178,3650"})
    @ParameterizedTest(name = "거리가 {0}일 때, 요금은 {1}원이다.")
    void calculate(int distance, int expectedFare) {
        //given
        final Path path = new Path(new Station("잠실역"), new Station("선릉역"), distance);
        final FareInfo fareInfo = new FareInfo(850, List.of(new PathEdgeProxy(path, 0)), 20);

        //when
        final FareInfo result = fareStrategy.calculate(fareInfo);

        //then
        assertThat(result.getFare()).isEqualTo(expectedFare);
    }
}
