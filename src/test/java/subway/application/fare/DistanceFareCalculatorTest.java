package subway.application.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.fare.Fare;

@SuppressWarnings("NonAsciiCharacters")
class DistanceFareCalculatorTest {

    private DistanceFareCalculator distanceFareCalculator;

    @BeforeEach
    void setUp() {
        distanceFareCalculator = new DistanceFareCalculator();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 7, 9, 10})
    void 이동거리가_10KM_이내면_추가요금은_없다(final int input) {
        final Fare fare = distanceFareCalculator.calculateFareByDistance(input);

        assertThat(fare.getFare()).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource(value = {"13:100", "16:200", "20:200", "50:800"}, delimiter = ':')
    void 이동거리가_10KM_이상_50KM_보다_작으면_5KM당_추가_요금이_발생한다(final int distance, final int expected) {
        final Fare fare = distanceFareCalculator.calculateFareByDistance(distance);

        assertThat(fare.getFare()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"51:900", "58:900", "59:1000"}, delimiter = ':')
    void 이동거리가_50KM_이상이면_8KM당_추가_요금이_발생한다(final int distance, final int expected) {
        final Fare fare = distanceFareCalculator.calculateFareByDistance(distance);

        assertThat(fare.getFare()).isEqualTo(expected);
    }

}
