package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FareCalculatorTest {

    @Test
    void 거리가_10km_미만이면_기본요금_1250원이다() {
        // given
        final int distance = 9;
        final FareCalculator subwayFareCalculator = new SubwayFareCalculator();

        // when
        final int fare = subwayFareCalculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @ParameterizedTest
    @CsvSource(value = {"10,1250", "11,1350", "15,1350", "16,1450", "50,2050"})
    void 거리가_10km_초과_50km_이하면_5km_마다_100원이_추가된다(final int distance, final int fare) {
        // when
        final FareCalculator subwayFareCalculator = new SubwayFareCalculator();
        final int result = subwayFareCalculator.calculate(distance);

        // then
        assertThat(result).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource(value = {"51, 2150", "58,2150"})
    void 거리가_50km_초과면_8km_마다_100원이_추가된다(final int distance, final int fare) {
        // when
        final FareCalculator subwayFareCalculator = new SubwayFareCalculator();
        final int result = subwayFareCalculator.calculate(distance);

        // then
        assertThat(result).isEqualTo(fare);
    }
}
