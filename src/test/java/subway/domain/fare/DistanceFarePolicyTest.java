package subway.domain.fare;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {

    // given
    final FarePolicy farePolicy = new DistanceFarePolicy();

    @Test
    void fare1() {
        // when
        final int fare = farePolicy.calculateFare(9);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    void fare2() {
        // when
        final int fare = farePolicy.calculateFare(12);

        // then
        assertThat(fare).isEqualTo(1350);
    }

    @Test
    void fare3() {
        // when
        final int fare = farePolicy.calculateFare(16);

        // then
        assertThat(fare).isEqualTo(1450);
    }

    @Test
    void fare4() {
        // when
        final int fare = farePolicy.calculateFare(58);

        // then
        assertThat(fare).isEqualTo(2150);
    }

}