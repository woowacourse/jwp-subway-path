package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTest {

    @ParameterizedTest
    @CsvSource({
            "10000, 35, 6500",
            "1000, 10, 900",
            "3500, 10, 3150"
    })
    @DisplayName("discountPercent() : 할인을 받고 난 금액을 계산할 수 있다.")
    void test_discountPercent(final int origin, final int percent, final int result) throws Exception {
        //given
        final Money originMoney = new Money(origin);
        final Money resultMoney = new Money(result);

        //when & then
        assertEquals(resultMoney, originMoney.calculateDiscountedPrice(percent));
    }

    @Test
    @DisplayName("constructor() : 돈이 음수가 되면 IllegalArgumentException가 발생합니다.")
    void test_constructor_IllegalArgumentException() throws Exception {
        //when & then
        assertThatThrownBy(() -> new Money(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("discountPercent() : 할인율이 0퍼센트이면 그대로 값을 반환합니다.")
    void test_discountPercent_zeroPercent() throws Exception {
        //given
        final Money money = new Money(10000);
        final int percentage = 0;

        //when & then
        assertEquals(money, money.calculateDiscountedPrice(percentage));
    }
}
