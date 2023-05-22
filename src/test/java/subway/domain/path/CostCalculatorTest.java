package subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CostCalculatorTest {

    @DisplayName("10KM 이하는 기본요금")
    @CsvSource(value = {"1:1250", "5:1250", "9:1250", "10:1250"}, delimiter = ':')
    @ParameterizedTest
    void under_10km(int distance, int expectedCost) {
        // when + then
        assertEquals(expectedCost, CostCalculator.calculateCost(distance));
    }

    @DisplayName("10KM 초과 50KM 이하")
    @CsvSource(value = {"14:1350", "15:1350", "19:1450", "20:1450", "24:1550", "25:1550", "29:1650", "30:1650", "34:1750", "35:1750", "39:1850", "40:1850", "44:1950", "45:1950", "49:2050", "50:2050"}, delimiter = ':')
    @ParameterizedTest
    void over_10km_under_50km(int distance, int expectedCost) {
        // when + then
        assertEquals(expectedCost, CostCalculator.calculateCost(distance));
    }

    @DisplayName("50KM 초과")
    @CsvSource(value = {"51:2150", "57:2150", "58:2150", "59:2250", "65:2250", "66:2250", "67:2350"}, delimiter = ':')
    @ParameterizedTest
    void over_50km(int distance, int expectedCost) {
        //when + then
        assertEquals(expectedCost, CostCalculator.calculateCost(distance));
    }
}
