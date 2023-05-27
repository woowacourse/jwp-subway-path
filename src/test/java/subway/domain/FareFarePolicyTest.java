package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.fare.Fare;
import subway.domain.fare.FarePolicy;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FareFarePolicyTest {

    @DisplayName("10km 이내의 운행요금을 계산한다")
    @Test
    void calculateFare_distanceUnder100() {
        // given
        int distance = 10;
        final FarePolicy farePolicy = FarePolicy.of(distance);

        // when
        final Fare fare = farePolicy.calculateFare(distance);

        // then
        assertThat(fare.getValue()).isEqualTo(1_250);
    }

    @DisplayName("10km 초과 50km 이하의 운행요금을 계산한다")
    @Test
    void calculateFare_distanceOver10Under50() {
        // given
        int distance = 12;
        final FarePolicy farePolicy = FarePolicy.of(distance);

        // when
        final Fare fare = farePolicy.calculateFare(distance);

        // then
        assertThat(fare.getValue()).isEqualTo(1_350);
    }

    @DisplayName("50km 초과의 운행요금을 계산한다")
    @Test
    void calculateFare_distanceOver50() {
        // given
        int distance = 58;
        final FarePolicy farePolicy = FarePolicy.of(distance);

        // when
        final Fare fare = farePolicy.calculateFare(distance);

        // then
        assertThat(fare.getValue()).isEqualTo(2_150);
    }
}
