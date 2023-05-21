package subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.path.PathEdgeProxy;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineAdditionalFareStrategyImplTest {

    private final LineAdditionalFareStrategyImpl lineAdditionalFareStrategy = new LineAdditionalFareStrategyImpl();

    @DisplayName("여러 노선 중 가장 높은 추가 금액이 적용된다")
    @Test
    void calculate() {
        //given
        final Line line1 = new Line("1호선", "blue", 500);
        final Line line2 = new Line("1호선", "blue", 300);
        final Line line3 = new Line("1호선", "blue", 1000);

        final PathEdgeProxy path1 = new PathEdgeProxy(line1, null);
        final PathEdgeProxy path2 = new PathEdgeProxy(line2, null);
        final PathEdgeProxy path3 = new PathEdgeProxy(line3, null);

        //when
        final int fare = lineAdditionalFareStrategy.calculate(List.of(path1, path2, path3));

        //then
        assertThat(fare).isEqualTo(1000);
    }

}
