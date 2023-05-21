package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StandardFarePolicyTest {

    private final FarePolicy farePolicy = new StandardFarePolicy();

    @CsvSource({"9, 1250", "10, 1250", "11, 1350", "15, 1350", "16, 1450", "50, 2050", "55, 2150", "58, 2150", "66, 2250"})
    @ParameterizedTest(name = "이동 거리가 {0}km일 때 {1}만큼의 요금이 부과된다.")
    void 거리에_맞는_요금을_반환한다(final int distance, final int fare) {
        // given
        int result = farePolicy.calculate(distance);

        // expect
        assertThat(result).isEqualTo(fare);
    }
}
