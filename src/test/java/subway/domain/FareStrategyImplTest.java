package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareStrategyImplTest {

    private FareStrategyImpl fareStrategy = new FareStrategyImpl();

    @DisplayName("요금 계산 테스트")
    @CsvSource(value = {"10,1250", "25,1550", "50,2050", "60,2250", "68,2350", "106,2750", "178,3650"})
    @ParameterizedTest
    void calculate(long distance, int expectedFare) {
        //when
        final int fare = fareStrategy.calculate(distance);

        //then
        assertThat(fare).isEqualTo(expectedFare);
    }
}
