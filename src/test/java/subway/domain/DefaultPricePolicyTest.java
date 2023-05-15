package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultPricePolicyTest {

    private SubwayPricePolicy subwayPricePolicy;

    @BeforeEach
    void setUp() {
        subwayPricePolicy = new DefaultPricePolicy();
    }

    @ParameterizedTest
    @CsvSource({
            "9,1250",
            "12,1350",
            "16,1450",
            "58,2150"
    })
    @DisplayName("calculate() : 거리에 따라 요금을 계산할 수 있다.")
    void test_calculate(final int distance, final int price) throws Exception {
        //when & then
        assertEquals(price, subwayPricePolicy.calculate(distance));
    }
}
