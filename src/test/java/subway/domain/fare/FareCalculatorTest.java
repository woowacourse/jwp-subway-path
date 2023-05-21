package subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FareCalculatorTest {

    @InjectMocks
    private FareCalculator fareCalculator;

    @Mock
    private DistanceFareStrategy distanceFareStrategy;
    @Mock
    private LineAdditionalFareStrategy lineAdditionalFareStrategy;

    @DisplayName("모든 정책을 반영한 요금을 계산할 수 있다")
    @Test
    void calculate() {
        //given
        when(distanceFareStrategy.calculate(anyLong()))
                .thenReturn(1250);
        when(lineAdditionalFareStrategy.calculate(anyList()))
                .thenReturn(1000);

        //when, then
        assertThat(fareCalculator.of(emptyList())).isEqualTo(2250);
    }
}
