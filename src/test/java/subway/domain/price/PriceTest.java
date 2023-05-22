package subway.domain.price;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @ParameterizedTest
    @ValueSource(longs = {0, 100, 10000})
    @DisplayName("금액이 정상적으로 생성되어야 한다.")
    void create_success(long input) {
        // given
        Price price = Price.from(input);

        // expect
        assertThat(price.getAmount())
                .isEqualTo(input);
    }

    @Test
    @DisplayName("금액이 0원 미만이면 예외가 발생해야 한다.")
    void create_lessThan0() {
        // expect
        assertThatThrownBy(() -> Price.from(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("금액은 0 이상이어야 합니다.");
    }

    @Test
    @DisplayName("금액은 금액끼리 더할 수 있어야 한다.")
    void plus_success() {
        // given
        Price price = Price.from(1000);

        // when
        Price newPrice = price.plus(Price.from(500));

        // then
        assertThat(newPrice.getAmount())
                .isEqualTo(1500);
    }

    @Test
    @DisplayName("금액은 금액끼리 뺄 수 있어야 한다.")
    void minus_success() {
        // given
        Price price = Price.from(1000);

        // when
        Price newPrice = price.minus(Price.from(500));

        // then
        assertThat(newPrice.getAmount())
                .isEqualTo(500);
    }

    @Test
    @DisplayName("금액을 뺄 때 빼는 값이 더 크면 예외가 발생해야 한다.")
    void minus_biggerThanSource() {
        // given
        Price price = Price.from(1000);
        Price otherPrice = Price.from(1001);

        // expect
        assertThatThrownBy(() -> price.minus(otherPrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 금액보다 더 큰 금액은 뺄 수 없습니다.");
    }

    @Test
    @DisplayName("금액을 뺄 때 같은 가격을 빼면, 0원이 되어야 한다.")
    void minus_samePrice() {
        // given
        Price price = Price.from(1000);
        Price otherPrice = Price.from(1000);

        // expect
        assertThat(price.minus(otherPrice))
                .isEqualTo(Price.ZERO);
    }

    @Test
    @DisplayName("금액을 곱할 수 있어야 한다.")
    void multiple_success() {
        // given
        Price price = Price.from(1000);

        // when
        Price multiplePrice = price.multiple(0.2);

        // then
        assertThat(multiplePrice.getAmount())
                .isEqualTo(200);
    }

    @Test
    @DisplayName("금액을 곱할 때, 0을 곱하면 0원이 되어야 한다.")
    void multiple_multipleZero() {
        // given
        Price price = Price.from(1000);

        // when
        Price mulitplePrice = price.multiple(0);

        // then
        assertThat(mulitplePrice)
                .isEqualTo(Price.ZERO);
    }
}
