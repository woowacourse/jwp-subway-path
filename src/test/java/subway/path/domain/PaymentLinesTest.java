package subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static subway.path.domain.AgeGroup.ADULTS;
import static subway.path.domain.AgeGroup.KIDS;
import static subway.path.domain.AgeGroup.SENIOR;
import static subway.path.domain.AgeGroup.TEENAGERS;
import static subway.path.domain.AgeGroup.TODDLER;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.payment.domain.discount.AgeGroupDiscountPolicy;
import subway.payment.domain.discount.DiscountResult;
import subway.payment.domain.payment.DefaultPaymentPolicy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("PaymentLines 은(는)")
class PaymentLinesTest {

    private final PaymentPolicy paymentPolicy = new DefaultPaymentPolicy();
    private final DiscountPolicy discountPolicy = new AgeGroupDiscountPolicy();
    private final Path path = mock(Path.class);

    @Test
    void 요금을_구할_수_있다() {
        // given
        given(path.totalDistance()).willReturn(58);
        final PaymentLines paymentLines = new PaymentLines(path, paymentPolicy, discountPolicy);

        // when
        final DiscountResult discountResult = paymentLines.discountFee();

        // then
        final Map<String, Integer> stringIntegerMap = discountResult.feeByDiscountInfo();
        assertThat(stringIntegerMap.get(TODDLER.info())).isEqualTo(0);
        assertThat(stringIntegerMap.get(KIDS.info())).isEqualTo(900);
        assertThat(stringIntegerMap.get(TEENAGERS.info())).isEqualTo(1440);
        assertThat(stringIntegerMap.get(ADULTS.info())).isEqualTo(2150);
        assertThat(stringIntegerMap.get(SENIOR.info())).isEqualTo(0);
    }
}
