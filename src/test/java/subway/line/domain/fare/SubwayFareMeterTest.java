package subway.line.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.line.domain.fare.application.faremeterpolicy.CustomerCondition;
import subway.line.domain.fare.application.SubwayFareMeter;
import subway.line.domain.section.domain.Distance;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SubwayFareMeterTest {
    @Autowired
    private SubwayFareMeter subwayFareMeter;

    @Test
    @DisplayName("10km 이내의 거리에 대한 요금은 기본 운임으로 계산한다.")
    void defaultFare() {
        assertThat(subwayFareMeter.calculateFare(new CustomerCondition(Distance.of(9))).getMoney())
                .isEqualTo(new BigDecimal("1250"));
    }

    @Test
    @DisplayName("10km 이상의 거리에 대한 요금은 기본 운임 + 5km마다 100월씩 추가해 계산한다.")
    void middleFare1() {
        assertThat(subwayFareMeter.calculateFare(new CustomerCondition(Distance.of(11))).getMoney())
                .as("5km마다 추가요금이 부과되므로 아직 추가요금은 없다.")
                .isEqualTo(new BigDecimal("1250.0"));
    }

    @Test
    @DisplayName("50km 이하의 거리에 대한 요금은 기본 운임 + 5km마다 100월씩 추가해 계산한다.")
    void middleFare2() {
        assertThat(subwayFareMeter.calculateFare(new CustomerCondition(Distance.of(49))).getMoney())
                .as("10km + 5km*7 + 4km = 49km이므로 추가요금 700원이 부과된다.")
                .isEqualTo(new BigDecimal("1950.0"));
    }

    @Test
    @DisplayName("50km를 초과하는 거리에 대한 요금은 기본 운임 + 8km마다 100원씩 추가하여 부과한다.")
    void maxFare() {
        assertThat(subwayFareMeter.calculateFare(new CustomerCondition(Distance.of(51))).getMoney())
                .as("10km + 8km*5 + 1km = 51km이므로 추가요금 500원이 부과된다.")
                .isEqualTo(new BigDecimal("1750.0"));
    }
}