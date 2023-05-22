package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.fare.Fare;
import subway.domain.line.Distance;

class DistanceFarePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"9,1250", "10, 1350", "12,1350", "16,1450", "50,2050", "58,2150"}, delimiter = ',')
    @DisplayName("거리에 비례하여 요금을 계산한다.")
    void calculateFareWithDistanceRate(int distance, int fare) {
        Fare calculatedFare = DistanceFarePolicy.calculateFare(new Distance(distance));

        assertThat(calculatedFare.getValue()).isEqualTo(fare);
    }
}
