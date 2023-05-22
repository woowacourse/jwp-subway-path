package subway.domain.fare;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.domain.fare.strategy.charge.ChargeStrategy;
import subway.domain.fare.strategy.discount.DiscountStrategy;
import subway.domain.subway.Line;

@ExtendWith(MockitoExtension.class)
class FareCalculatorTest {
    @Mock
    ChargeStrategy chargeStrategy;

    @Mock
    DiscountStrategy discountStrategy;

    @Test
    void calculate() {
        when(chargeStrategy.calculate(any())).thenReturn(0);
        when(discountStrategy.discount(eq(1250), any())).thenReturn(1000);

        assertThat(
            new FareCalculator(
                List.of(chargeStrategy),
                List.of(discountStrategy))
                .calculate(
                    new FareInfo(
                    20,
                    Set.of(new Line(), new Line()),
                    25)
                )
        ).isEqualTo(1000);
    }
}
