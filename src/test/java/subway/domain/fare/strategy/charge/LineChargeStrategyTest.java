package subway.domain.fare.strategy.charge;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.domain.fare.FareInfo;
import subway.domain.subway.Line;

@ExtendWith(MockitoExtension.class)
class LineChargeStrategyTest {
    @Mock
    FareInfo fareInfo;

    @Test
    void calculate() {
        when(fareInfo.getLines()).thenReturn(
            Set.of(
                new Line("1호선", "파랑", 600),
                new Line("2호선", "초록", 200),
                new Line("3호선", "주황", 300),
                new Line("4호선", "하늘", 0)
            ));
        LineChargeStrategy lineChargeStrategy = new LineChargeStrategy();
        assertThat(lineChargeStrategy.calculate(fareInfo)).isEqualTo(600);
    }
}
