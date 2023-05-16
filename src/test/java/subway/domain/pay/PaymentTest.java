package subway.domain.pay;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.Distance;
import subway.domain.Money;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.MoneyFixture.비용;

class PaymentTest {
    @Test
    void _0km_초과_10km_이하_거리_요금을_게산한다() {
        // when
        final Money totalPrice = Payment.calculate(new Distance(10));

        // then
        assertThat(totalPrice).isEqualTo(비용(1250D));
    }

    @ParameterizedTest
    @CsvSource(value = {"11:1250", "15:1350", "20:1450"}, delimiter = ':')
    void _10km_초과_50km_이하_거리_요금을_게산한다(final int distance, final int price) {
        // when
        final Money totalPrice = Payment.calculate(new Distance(distance));

        // then
        assertThat(totalPrice).isEqualTo(비용(price));
    }
}
