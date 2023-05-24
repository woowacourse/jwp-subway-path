package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.fare.distanceproportion.TotalDistanceFareCalculator;

@SuppressWarnings("NonAsciiCharacters")
class TotalDistanceFareCalculatorTest {

    private static final int BASE_FARE = 1250;

    private TotalDistanceFareCalculator totalDistanceFareCalculator;

    @BeforeEach
    void setUp() {
        totalDistanceFareCalculator = new TotalDistanceFareCalculator();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 7, 9, 10})
    void 이동거리가_10KM_이내면_기본요금이_부과된다(final int input) {
        final Fare fare = totalDistanceFareCalculator.calculateFareByDistance(input);

        assertThat(fare.getFare()).isEqualTo(BASE_FARE);
    }

    @ParameterizedTest
    @CsvSource(value = {"13:1350", "16:1450", "20:1450", "50:2050"}, delimiter = ':')
    void 이동거리가_10KM_이상_50KM_보다_작으면_5KM당_추가_요금이_발생한다(final int distance, final int expected) {
        final Fare fare = totalDistanceFareCalculator.calculateFareByDistance(distance);

        assertThat(fare.getFare()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"51:2150", "58:2150", "59:2250"}, delimiter = ':')
    void 이동거리가_50KM_이상이면_5KM당_추가_요금이_발생한다(final int distance, final int expected) {
        final Fare fare = totalDistanceFareCalculator.calculateFareByDistance(distance);

        assertThat(fare.getFare()).isEqualTo(expected);
    }

}
