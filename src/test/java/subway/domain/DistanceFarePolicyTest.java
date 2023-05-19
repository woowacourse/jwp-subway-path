package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceFarePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"9,1250", "10, 1350", "12,1350", "16,1450", "50,2050", "58,2150"}, delimiter = ',')
    @DisplayName("거리에 비례하여 요금을 계산한다.")
    void calculateFareWithDistanceRate(int distance, int fare) {
        FarePolicy farePolicy = new DistanceFarePolicy();

        Fare calculatedFare = farePolicy.calculate(new Distance(distance));

        assertThat(calculatedFare.getValue()).isEqualTo(fare);
    }
}
