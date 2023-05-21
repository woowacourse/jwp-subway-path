package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.fare.FarePolicy;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FarePolicyTest {

    @DisplayName("10km 이내의 운행요금을 계산한다")
    @Test
    void calculateFare_distanceUnder100() {
        // given
        final FarePolicy farePolicy = new FarePolicy();
        int distance = 10;

        // when
        final int fare = farePolicy.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(1_250);
    }

    @DisplayName("10km 초과 50km 이하의 운행요금을 계산한다")
    @Test
    void calculateFare_distanceOver10Under50() {
        // given
        final FarePolicy farePolicy = new FarePolicy();
        int distance = 12;

        // when
        final int fare = farePolicy.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(1_350);
    }

    @DisplayName("50km 초과의 운행요금을 계산한다")
    @Test
    void calculateFare_distanceOver50() {
        // given
        final FarePolicy farePolicy = new FarePolicy();
        int distance = 58;

        // when
        final int fare = farePolicy.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(2_150);
    }
}
