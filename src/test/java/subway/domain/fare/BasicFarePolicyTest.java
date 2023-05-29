package subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BasicFarePolicyTest {

    @Test
    @DisplayName("기본 요금은 1250원이다.")
    void calculateBasicFareTest() {
        // given
        FarePolicy farePolicy = new BasicFarePolicy();
        int expectFare = 1250;

        // when
        int fare = farePolicy.calculateFare(100, Set.of());

        // then
        assertThat(fare).isEqualTo(expectFare);
    }

}