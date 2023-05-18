package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.Distance;

class DistanceProportionalFarePolicyTest {

    private final FarePolicy farePolicy = new DistanceProportionalFarePolicy();

    @DisplayName("거리에 따라 요금을 계산한다.")
    @Nested
    class calculate {

        @DisplayName("총 거리가 10보다 작은 경우")
        @ParameterizedTest(name = "거리가 {0}이면 요금은 1250이어야 한다.")
        @ValueSource(ints = {1, 2, 3})
        void distanceUnder10(final int distance) {
            final Fare totalFare = farePolicy.calculate(new Distance(distance));

            assertThat(totalFare.getValue())
                    .isEqualTo(1250);
        }

        @DisplayName("총 거리가 10보다 크고, 50보다 작거나 같은 경우")
        @ParameterizedTest(name = "거리가 {0}인 경우 요금은 {1}이어야 한다.")
        @CsvSource({"12,1350", "16,1450", "50,2050"})
        void distanceOver10Under50(final int distance, final int expectedFare) {
            final Fare totalFare = farePolicy.calculate(new Distance(distance));

            assertThat(totalFare.getValue())
                    .isEqualTo(expectedFare);
        }

        @DisplayName("총 거리가 50보다 큰 경우")
        @ParameterizedTest(name = "거리가 {0}인 경우 요금은 {1}이어야 한다.")
        @CsvSource({"58,2150", "59,2250", "80,2450"})
        void distanceOver50(final int distance, final int expectedFare) {
            final Fare totalFare = farePolicy.calculate(new Distance(distance));

            assertThat(totalFare.getValue())
                    .isEqualTo(expectedFare);
        }
    }
}
