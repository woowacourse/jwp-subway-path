package subway.route.domain.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("거리 계산 정책은")
class DistancePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"9,1250", "12,1350", "16,1450", "58,2150"})
    void 거리에_따른_요금을_계산한다(int input, long expected) {
        // given
        DistancePolicy distancePolicy = DistancePolicy.from(input);

        // when
        long result = distancePolicy.calculateFare(input);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
