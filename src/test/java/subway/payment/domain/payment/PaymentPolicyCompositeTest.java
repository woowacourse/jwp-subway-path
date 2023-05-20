package subway.payment.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.domain.fixture.StationFixture.역1;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역3;
import static subway.line.domain.fixture.StationFixture.역4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.path.domain.Path;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("PaymentPolicyComposite 은(는)")
class PaymentPolicyCompositeTest {

    private final PaymentPolicyComposite paymentPolicyComposite
            = new PaymentPolicyComposite(new DefaultPaymentPolicy(), new LineSurchargePaymentPolicy());

    private final Path path = new Path(
            new Line("1호선", 0, new Section(역1, 역2, 30)),
            new Line("2호선", 500, new Section(역2, 역3, 10)),
            new Line("3호선", 3000, new Section(역3, 역4, 10))
    );

    @Test
    void 등록된_PaymentPolicy_들의_금액_계산_결과의_합으로_계산한다() {
        // when
        final int fee = paymentPolicyComposite.calculateFee(path);

        // then
        assertThat(fee).isEqualTo(5050); // 3000 + 2050(50km)
    }
}
