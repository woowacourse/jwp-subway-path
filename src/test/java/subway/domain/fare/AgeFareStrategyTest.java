package subway.domain.fare;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.strategy.AgeFareStrategy;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class AgeFareStrategyTest {

    private final AgeFareStrategy ageFareStrategy = new AgeFareStrategy();

    @CsvSource(value = {"5,250", "13,250", "14,400", "19,400", "20, 850"})
    @ParameterizedTest(name = "{0}살이면 요금은 {1}원이다.")
    void calculate(final int age, final int expectedFare) {
        //given
        final FareInfo fareInfo = new FareInfo(850, emptyList(), age);

        //when
        final FareInfo result = ageFareStrategy.calculate(fareInfo);

        //then
        assertThat(result.getFare()).isEqualTo(expectedFare);
    }
}
