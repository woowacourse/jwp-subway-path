package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.strategy.DistanceBasicFareStrategy;
import subway.domain.fare.strategy.DistanceFiftyStrategy;
import subway.domain.fare.strategy.DistanceTenFareStrategy;
import subway.domain.section.Distance;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class DistanceFareStrategyTest {

    private final DistanceFareStrategies strategies = new DistanceFareStrategies(
            List.of(
                    new DistanceBasicFareStrategy(),
                    new DistanceTenFareStrategy(),
                    new DistanceFiftyStrategy())
    );


    @Test
    void 거리가_10키로_이내이면_1250원이다() {
        // given
        final Distance distance = new Distance(9);

        // when
        final Fare fare = strategies.getTotalFare(distance);

        // then
        assertThat(fare.getFare()).isEqualTo(1250);
    }

    @ParameterizedTest
    @CsvSource(value = {"10:1350", "15:1350", "50:2050"}, delimiter = ':')
    void 거리가_10에서_50_이내이면_5km_마다_100원_추가된다(final int distance, final int fare) {
        // given
        final Distance totalDistance = new Distance(distance);

        // when
        final Fare totalfare = strategies.getTotalFare(totalDistance);

        // then
        assertThat(totalfare.getFare()).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource(value = {"58:2150", "66:2250"}, delimiter = ':')
    void 거리가_50초과면_8km_마다_100원_추가된다(final int distance, final int fare) {
        // given
        final Distance totalDistance = new Distance(distance);

        // when
        final Fare totalfare = strategies.getTotalFare(totalDistance);

        // then
        assertThat(totalfare.getFare()).isEqualTo(fare);
    }

}
