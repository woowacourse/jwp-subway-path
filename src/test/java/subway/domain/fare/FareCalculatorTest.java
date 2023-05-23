package subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import subway.domain.fare.strategy.FareStrategy;

import java.util.List;

import static org.mockito.Mockito.*;

class FareCalculatorTest {

    private final FareStrategy fareStrategy1 = mock(FareStrategy.class);
    private final FareStrategy fareStrategy2 = mock(FareStrategy.class);

    private final FareCalculator fareCalculator = new FareCalculator(List.of(fareStrategy1, fareStrategy2));

    @DisplayName("모든 정책을 반영한 요금을 계산할 수 있다")
    @Test
    void calculate() {
        //when
        fareCalculator.of(null);

        //then
        final InOrder inOrder = inOrder(fareStrategy1, fareStrategy2);
        inOrder.verify(fareStrategy1, times(1)).calculate(any());
        inOrder.verify(fareStrategy2, times(1)).calculate(any());
    }
}
