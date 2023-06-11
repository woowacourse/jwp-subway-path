package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.DistanceFarePolicy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class DistanceFarePolicyTest {

    private static final int STANDARD_FARE = 1_250;

    @ParameterizedTest(name = "이용거리가 {0}km 일때, 운임 요금은 {1}원이다")
    @CsvSource(value = {
            "10, 1250",
            "12, 1350",
            "16, 1450",
            "50, 2050",
            "58, 2150",
    })
    void 이용거리에_따른_운임_요금을_계산한다(final Long distance, final int expectedFare) {
        // given
        final DistanceFarePolicy distanceFarePolicy = new DistanceFarePolicy();

        // when
        final int fare = distanceFarePolicy.calculate(distance, STANDARD_FARE);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}
