package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.AgeFarePolicy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class AgeFarePolicyTest {

    private static final int STANDARD_FARE = 1_250;

    @ParameterizedTest(name = "{0}세일 때, 운임 요금은 {1}원이다")
    @CsvSource(value = {
            "0, 0",
            "5, 0",
            "6, 450",
            "12, 450",
            "13, 720",
            "18, 720",
            "19, 1250",
            "24, 1250"
    })
    void 나이에_따른_운임_요금을_계산한다(final int age, final int expectedFare) {
        // given
        final AgeFarePolicy ageFarePolicy = AgeFarePolicy.from(age);

        // when
        final int actual = ageFarePolicy.calculate(STANDARD_FARE);

        // then
        assertThat(actual).isEqualTo(expectedFare);
    }
}
