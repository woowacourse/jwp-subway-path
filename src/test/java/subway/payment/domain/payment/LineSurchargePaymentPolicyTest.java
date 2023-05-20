package subway.payment.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.domain.fixture.StationFixture.역1;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역3;
import static subway.line.domain.fixture.StationFixture.역5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.path.domain.Path;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineSurchargePaymentPolicy 은(는)")
class LineSurchargePaymentPolicyTest {

    private final LineSurchargePaymentPolicy paymentPolicy = new LineSurchargePaymentPolicy();

    private final Path path = new Path(
            new Line("1호선", 0, new Section(역1, 역2, 10)),
            new Line("2호선", 500, new Section(역3, 역2, 10)),
            new Line("3호선", 3000, new Section(역3, 역5, 10)),
            new Line("5호선", 2000, new Section(역3, 역5, 10)),
            new Line("6호선", 2000, new Section(역1, 역5, 10))
    );

    @Test
    void 추가요금이_있는_노선이_여러개인_경우_가장_높은_금액의_추가_요금만_적용된다() {
        // when
        final int fee = paymentPolicy.calculateFee(path);

        // then
        assertThat(fee).isEqualTo(3000);
    }
}
