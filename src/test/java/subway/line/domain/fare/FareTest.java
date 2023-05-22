package subway.line.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class FareTest {

    @Test
    @DisplayName("생성하면 기본 요금을 들고있다.")
    void defaultFare() {
        final var fare = new Fare();
        assertThat(fare.getMoney()).isEqualTo(new BigDecimal("1250"));
    }

    @Test
    @DisplayName("주어진 숫자만큼 기본요금에 추가 요금을 더한다.")
    void multiplyFare() {
        final var fare = new Fare();
        assertThat(fare.addSurchargeMultipliedBy(8).getMoney())
                .isEqualTo(new BigDecimal("2050.0"));
    }
}