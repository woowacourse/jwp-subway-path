package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistancePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"12,100", "16,200", "50,800", "58,800"})
    @DisplayName("10km~50km는 5km마다 100원이 추가된다.")
    void baseTen(final int distance, final long additionFare) {
        final DistancePolicy distancePolicy = DistancePolicy.BASE_TEN;

        final long result = distancePolicy.calculateAdditionFare(distance);

        assertThat(result).isEqualTo(additionFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"50,0", "55,100", "58,100", "66,200"})
    @DisplayName("50km부터는 8km마다 100원이 추가된다.")
    void baseFifty(final int distance, final long additionFare) {
        final DistancePolicy distancePolicy = DistancePolicy.BASE_FIFTY;

        final long result = distancePolicy.calculateAdditionFare(distance);

        assertThat(result).isEqualTo(additionFare);
    }
}
