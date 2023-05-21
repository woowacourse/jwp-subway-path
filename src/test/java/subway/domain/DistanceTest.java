package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceTest {

    @ParameterizedTest
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "15:1350", "16:1450", "50:2050", "51:2150",
        "56:2150", "58:2150", "59:2250"}, delimiter = ':')
    @DisplayName("calculateCharge()를 호출하면 거리에 따른 요금을 반환한다.")
    void calculateCharge(int distanceValue, int chargeValue) {
        // given
        Distance distance = new Distance(distanceValue);
        Charge expected = new Charge(chargeValue);
        Charge zero = new Charge(0);

        // when
        Charge actual = distance.calculateCharge(zero);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 200, 300, 400})
    @DisplayName("calculateCharge()를 호출할 때 할증이 0원이 아니라면 거리에 따른 요금에 할증만큼 더해진 요금을 반환한다.")
    void calculateCharge_extra_charge(int extraChargeValue) {
        // given
        Distance distance = new Distance(26);
        Charge extraCharge = new Charge(extraChargeValue);
        Charge expected = new Charge(1650 + extraChargeValue);
        // when
        Charge actual = distance.calculateCharge(extraCharge);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}