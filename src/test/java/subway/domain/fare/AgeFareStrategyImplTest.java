package subway.domain.fare;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFareStrategyImplTest {

    private final AgeFareStrategyImpl ageFareStrategy = new AgeFareStrategyImpl();

    @CsvSource(value = {"5,250", "13,250", "14,400", "19,400", "20, 850"})
    @ParameterizedTest(name = "{0}살이면 요금은 {1}원이다.")
    void calculate(final int age, final int expectedFare) {
        //given
        final int fare = 850;

        //when
        final int result = ageFareStrategy.calculate(age, fare);

        //then
        assertThat(result).isEqualTo(expectedFare);
    }
}
