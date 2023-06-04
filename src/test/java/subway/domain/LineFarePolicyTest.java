package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.LineFarePolicy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class LineFarePolicyTest {

    private static final int STANDARD_FARE = 1_250;

    @ParameterizedTest(name = "이용한 노선 중 최대 추가 운임이 {0}원일 때, 운임 요금은 {1}원이다")
    @CsvSource(value = {
            "0, 1250",
            "1000, 2250"
    })
    void 노선_추가_운임에_따른_운임_요금을_계산한다(final int maxAdditionalFare, final int expectedFare) {
        // given
        final LineFarePolicy lineFarePolicy = new LineFarePolicy();

        // when
        final int actual = lineFarePolicy.calculate(maxAdditionalFare, STANDARD_FARE);

        // then
        assertThat(actual).isEqualTo(expectedFare);
    }
}
