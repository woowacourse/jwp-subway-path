package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.section.Distance;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class DistanceFareStrategyTest {

    @Test
    void 거리가_10키로_이내이면_1250원이다() {
        // given
        final Distance distance = new Distance(9);

        // when
        final Fare fare = new DistanceFareStrategy().calculate(distance);

        // then
        assertThat(fare.getFare()).isEqualTo(1250);
    }

    @Test
    void 거리가_10에서_50_이내이면_5km_마다_100원_추가된다() {
        // given
        final Distance distance = new Distance(16);

        // when
        final Fare fare = new DistanceFareStrategy().calculate(distance);

        // then
        assertThat(fare.getFare()).isEqualTo(1450);
    }

    @Test
    void 거리가_50초과면_8km_마다_100원_추가된다() {
        // given
        final Distance distance = new Distance(58);

        // when
        final Fare fare = new DistanceFareStrategy().calculate(distance);

        // then
        assertThat(fare.getFare()).isEqualTo(2250);
    }

}
