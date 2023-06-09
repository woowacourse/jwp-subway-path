package subway.route.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.route.domain.FareCalculator;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("요금 계산 기능은")
class FareCalculatorTest {

    @DisplayName("거리에 따른 요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {"10,1250", "50,2050", "51,2150", "80,2450", "81,2450", "100,2750"})
    void calculateFare(final int input, final long expected) {
        // given
        final FareCalculator fareCalculator = new FareCalculator();

        // when
        final long result = fareCalculator.calculateFare(input);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
