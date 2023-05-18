package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ChargeTest {

    @ParameterizedTest
    @CsvSource(value = {"1250:720", "1350:800", "1450:880"}, delimiter = ':')
    @DisplayName("discount()를 호출할 때 청소년 할인율을 입력하면 청소년 요금 정책에 따라 할인된 값을 반환한다.")
    void discount_teenager(int principalValue, int expected) {
        // given
        Charge principal = new Charge(principalValue);
        // when
        int actual = principal.discount(DiscountRate.TEENAGER_DISCOUNT_RATE).getValue();

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"1250:450", "1350:500", "1450:550"}, delimiter = ':')
    @DisplayName("discount()를 호출할 때 어린이 할인율을 입력하면 어린이 요금 정책에 따라 할인된 값을 반환한다.")
    void discount_child(int principalValue, int expected) {
        // given
        Charge principal = new Charge(principalValue);
        // when
        int actual = principal.discount(DiscountRate.CHILD_DISCOUNT_RATE).getValue();

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
