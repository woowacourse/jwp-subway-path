package subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.fare.strategy.LineAdditionalFareStrategy;
import subway.domain.path.PathEdgeProxy;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineAdditionalFareStrategyTest {

    private final LineAdditionalFareStrategy lineAdditionalFareStrategy = new LineAdditionalFareStrategy();

    @DisplayName("여러 노선 중 가장 높은 추가 금액이 적용된다")
    @Test
    void calculate() {
        //given
        final List<PathEdgeProxy> shortest = List.of(new PathEdgeProxy(null, 500),
                new PathEdgeProxy(null, 300),
                new PathEdgeProxy(null, 200));

        final FareInfo fareInfo = new FareInfo(850, shortest, 20);

        //when
        final FareInfo result = lineAdditionalFareStrategy.calculate(fareInfo);

        //then
        assertThat(result.getFare()).isEqualTo(1350);
    }

}
