package subway.route.domain.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MiddleDistancePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"10,1250", "18,1450", "11,1350", "12,1350", "13,1350", "20,1450", "58,2050"})
    void 거리_10_이상에서_5키로마다_100원_추가(int input, long expected) {
        DistanceFareStrategy overTenKMStrategy = new MiddleDistancePolicy();

        long result = overTenKMStrategy.calculateFare(input);

        assertThat(result).isEqualTo(expected);
    }
}
