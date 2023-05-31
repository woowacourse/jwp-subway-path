package subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.path.Path;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class BasicFarePolicyTest {

    @Test
    @DisplayName("기본 요금은 1250원이다.")
    void calculateBasicFareTest() {
        // given
        FarePolicy farePolicy = new BasicFarePolicy();
        Path tempPath = new Path(new ArrayList<>(), new ArrayList<>(), 5);
        int expectFare = 1250;

        // when
        int fare = farePolicy.calculateFare(tempPath);

        // then
        assertThat(fare).isEqualTo(expectFare);
    }

}