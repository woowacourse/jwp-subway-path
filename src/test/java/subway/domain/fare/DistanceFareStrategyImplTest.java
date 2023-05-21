package subway.domain.fare;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFareStrategyImplTest {

    private final DistanceFareStrategyImpl fareStrategy = new DistanceFareStrategyImpl();

    @CsvSource(value = {"10,1250", "25,1550", "50,2050", "60,2250", "68,2350", "106,2750", "178,3650"})
    @ParameterizedTest(name = "거리가 {0}일 때, 요금은 {1}원이다.")
    void calculate(long distance, int expectedFare) {
        //when
        final int fare = fareStrategy.calculate(distance);

        //then
        assertThat(fare).isEqualTo(expectedFare);
    }
}
