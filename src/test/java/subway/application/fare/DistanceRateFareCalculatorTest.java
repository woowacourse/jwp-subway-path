package subway.application.fare;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.Distance;
import subway.domain.Fare;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class DistanceRateFareCalculatorTest {

    private DistanceRateFareCalculator distanceRateFareCalculator;

    @BeforeEach
    void init() {
        final List<DistanceRateFare> distanceRateFares = List.of(new DistanceOver10Under50(), new DistanceUnder10(), new DistanceOver50());
        distanceRateFareCalculator = new DistanceRateFareCalculator(distanceRateFares);
    }

    @ParameterizedTest(name = "길이 : {0}")
    @CsvSource(value = {"10:1250", "12:1350", "16:1450", "50:2050", "51:2150", "58:2150", "60:2250"}, delimiter = ':')
    void 거리에_따른_요금을_구한다(final int distance, final int expectedFare) {
        // given
        final Distance target = Distance.from(distance);
        final FareCondition fareCondition = FareCondition.from(target);

        // when
        final Fare result = distanceRateFareCalculator.calculateFare(fareCondition);

        // then
        assertThat(result).isEqualTo(Fare.from(expectedFare));
    }
}
