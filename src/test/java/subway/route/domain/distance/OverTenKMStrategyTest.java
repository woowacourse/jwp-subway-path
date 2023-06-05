package subway.route.domain.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OverTenKMStrategyTest {

    @ParameterizedTest
    @CsvSource(value = {"10,0", "18,200", "11,100", "12,100", "13,100", "20,200", "58,800"})
    void 거리_10_이상에서_5키로마다_100원_추가(final int input, final long expected) {
        final DistanceFareStrategy overTenKMStrategy = new OverTenKMStrategy();

        final long result = overTenKMStrategy.calculateFare(input);

        assertThat(result).isEqualTo(expected);
    }
}
