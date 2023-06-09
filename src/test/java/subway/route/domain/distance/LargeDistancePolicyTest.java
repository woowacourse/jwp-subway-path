package subway.route.domain.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("50킬로_이상 요금 계산 전략 테스트")
class LargeDistancePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"50,2050", "58,2150", "51,2150", "52,2150", "53,2150", "59,2250"})
    void 거리_50_이상에서_8키로마다_100원_추가(int input, long expected) {
        DistanceFareStrategy overFiftyKMStrategy = new LargeDistancePolicy();

        long result = overFiftyKMStrategy.calculateFare(input);

        assertThat(result).isEqualTo(expected);
    }
}
