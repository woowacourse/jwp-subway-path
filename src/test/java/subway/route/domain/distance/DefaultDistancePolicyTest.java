package subway.route.domain.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("거리 계산 정책 테스트")
class DefaultDistancePolicyTest {

    @Test
    void 거리_10_이하_요금_계산() {
        final DistanceFareStrategy distanceFareStrategy = new DefaultDistancePolicy();

        final long result = distanceFareStrategy.calculateFare(10);

        assertThat(result).isEqualTo(1250);
    }
}
