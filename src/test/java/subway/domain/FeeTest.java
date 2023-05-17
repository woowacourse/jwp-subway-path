package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FeeTest {

    @DisplayName("기본요금 테스트")
    @Test
    void getDefaultFee() {
        Fee fee = Fee.toDistance(5);

        assertThat(fee.getFee()).isEqualTo(1250);
    }

    @DisplayName("10km ~ 49km 까지 5km 당 100원 할증 테스트")
    @ParameterizedTest
    @CsvSource(value = {
            "10:1350",
            "14:1350",
            "15:1450",
            "19:1450",
            "20:1550",
            "24:1550",
            "25:1650",
            "29:1650",
            "30:1750",
            "34:1750",
            "35:1850",
            "39:1850",
            "40:1950",
            "44:1950",
            "45:2050",
            "49:2050",

    }, delimiter = ':')
    void getMiddleSurchargeFee(int distance, int expectedFee) {
        Fee fee = Fee.toDistance(distance);

        assertThat(fee.getFee()).isEqualTo(expectedFee);
    }

    @DisplayName("50km 이상 8km 당 100원 할증 테스트")
    @ParameterizedTest
    @CsvSource(value = {
            "50:2150",
            "57:2150",
            "58:2250",
            "65:2250"
    }, delimiter = ':')
    void getPremiumSurchargeFee(int distance, int expectedFee) {
        Fee fee = Fee.toDistance(distance);

        assertThat(fee.getFee()).isEqualTo(expectedFee);
    }

}