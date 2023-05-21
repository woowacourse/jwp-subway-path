package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.section.Distance;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BasicFarePolicyTest {

    @ParameterizedTest
    @CsvSource({"10, 1250", "12, 1350", "16, 1450", "50, 2050", "58, 2150"})
    void 거리에_따라_요금을_계산한다(final int value, final int expectedFare) {
        //given
        final FarePolicy farePolicy = new BasicFarePolicy();
        final Distance distance = new Distance(value);

        //when
        final int actualFare = farePolicy.calculate(distance);

        //then
        assertThat(actualFare).isEqualTo(expectedFare);
    }

}
