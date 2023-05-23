package subway.cenario;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.line.domain.fare.Fare;
import subway.line.domain.fare.application.SubwayFareMeter;
import subway.line.domain.fare.application.domain.Age;
import subway.line.domain.fare.dto.CustomerCondition;
import subway.line.domain.section.domain.Distance;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FareMeterPolicyTest {
    @Autowired
    private SubwayFareMeter subwayFareMeter;

    @Test
    @DisplayName("10km 이내의 거리에 대한 요금은 기본 운임으로 계산한다.")
    void defaultFare() {
        final var customerCondition = new CustomerCondition(Distance.of(9), List.of(), Age.of(20));

        assertThat(subwayFareMeter.calculateFare(customerCondition).getMoney().intValue())
                .isEqualTo(1250);
    }

    @Test
    @DisplayName("10km 이상의 거리에 대한 요금은 기본 운임 + 5km마다 100월씩 추가해 계산한다.")
    void middleFare1() {
        final var customerCondition = new CustomerCondition(Distance.of(11), List.of(), Age.of(20));

        assertThat(subwayFareMeter.calculateFare(customerCondition).getMoney().intValue())
                .as("5km마다 추가요금이 부과되므로 아직 추가요금은 없다.")
                .isEqualTo(1250);
    }

    @Test
    @DisplayName("50km 이하의 거리에 대한 요금은 기본 운임 + 5km마다 100월씩 추가해 계산한다.")
    void middleFare2() {
        final var customerCondition = new CustomerCondition(Distance.of(49), List.of(), Age.of(20));

        assertThat(subwayFareMeter.calculateFare(customerCondition).getMoney().intValue())
                .as("10km + 5km*7 + 4km = 49km이므로 추가요금 700원이 부과된다.")
                .isEqualTo(1950);
    }

    @Test
    @DisplayName("50km를 초과하는 거리에 대한 요금은 기본 운임 + 8km마다 100원씩 추가하여 부과한다.")
    void maxFare() {
        final var customerCondition = new CustomerCondition(Distance.of(51), List.of(), Age.of(20));

        assertThat(subwayFareMeter.calculateFare(customerCondition).getMoney().intValue())
                .as("10km + 8km*5 + 1km = 51km이므로 추가요금 500원이 부과된다.")
                .isEqualTo(1750);
    }

    @Test
    @DisplayName("추가요금이 있는 노선을 기본 요금만큼 이용하면 기본 요금에 추가요금이 추가된다.")
    void surchargeDefault() {
        final var surcharge = new Fare(new BigDecimal("900"));
        final var customerCondition = new CustomerCondition(Distance.of(9), List.of(surcharge), Age.of(20));

        assertThat(subwayFareMeter.calculateFare(customerCondition).getMoney().intValue())
                .as("기본요금 1,250원에 900원의 추가요금이 붙는다.")
                .isEqualTo(2150);
    }

    @Test
    @DisplayName("추가요금이 있는 노선을 기본요금 이상으로 이용하면 추가요금이 부과된 금액에 노선 추가금액이 또 붙는다.")
    void surchargeWithSurcharge() {
        final var surcharge = new Fare(new BigDecimal("900"));
        final var customerCondition = new CustomerCondition(Distance.of(51), List.of(surcharge), Age.of(20));

        assertThat(subwayFareMeter.calculateFare(customerCondition).getMoney().intValue())
                .as("1,750원에 900원의 추가요금이 붙는다.")
                .isEqualTo(2650);
    }

    @Test
    @DisplayName("추가요금이 있는 노선을 여러 개 지나며 이용할 경우 가장 높은 금액의 추가 요금만 적용된다.")
    void maxSurcharge() {
        final var surcharge = new Fare(new BigDecimal("900"));
        final var surcharge2 = new Fare(new BigDecimal("1100"));
        final var surcharge3 = new Fare(new BigDecimal("1300"));
        final var customerCondition = new CustomerCondition(Distance.of(51), List.of(surcharge, surcharge2, surcharge3), Age.of(20));

        assertThat(subwayFareMeter.calculateFare(customerCondition).getMoney().intValue())
                .as("1750원에 1300원의 추가요금이 붙는다.")
                .isEqualTo(3050);
    }

    @Test
    @DisplayName("청소년은 운임에서 350원을 공제한 금액의 20%을 할인한다.")
    void teenager() {
        final var surcharge = new Fare(new BigDecimal("900"));
        final var customerCondition = new CustomerCondition(Distance.of(51), List.of(surcharge), Age.of(15));

        assertThat(subwayFareMeter.calculateFare(customerCondition).getMoney().intValue())
                .as("1750원에서 350원을 뺀 금액의 20% 할인 가격이 최종 요금이다.")
                .isEqualTo(1840);
    }

    @Test
    @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50%을 할인한다.")
    void child() {
        final var surcharge = new Fare(new BigDecimal("900"));
        final var customerCondition = new CustomerCondition(Distance.of(51), List.of(surcharge), Age.of(8));

        assertThat(subwayFareMeter.calculateFare(customerCondition).getMoney().intValue())
                .as("1750원에서 350원을 뺀 금액의 50% 할인 가격이 최종 요금이다.")
                .isEqualTo(1150);
    }
}
