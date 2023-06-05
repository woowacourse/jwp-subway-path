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
class OverFiftyKMStrategyTest {

    @ParameterizedTest
    @CsvSource(value = {"50,0", "58,100", "51,100", "52,100", "53,100", "59,200"})
    void 거리_50_이상에서_8키로마다_100원_추가(final int input, final long expected) {
        final DistanceFareStrategy overFiftyKMStrategy = new OverFiftyKMStrategy();

        final long result = overFiftyKMStrategy.calculateFare(input);

        assertThat(result).isEqualTo(expected);
    }
}
