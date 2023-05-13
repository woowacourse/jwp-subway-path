package subway.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.Lines;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("PaymentLines 은(는)")
class PaymentLinesTest {

    private final PaymentPolicy paymentPolicy = new DefaultPaymentPolicy();
    private final Lines lines = mock(Lines.class);

    @Test
    void 요금을_구할_수_있다() {
        // given
        given(lines.totalDistance()).willReturn(58);
        final PaymentLines paymentLines = new PaymentLines(lines, paymentPolicy);

        // when
        final int fee = paymentLines.calculateFee();

        // then
        assertThat(fee).isEqualTo(2150);
    }
}
