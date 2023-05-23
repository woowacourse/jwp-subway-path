package subway.domain.fare.discount;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import subway.domain.vo.Money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import static subway.domain.PassengerType.ADULT;

@DisplayNameGeneration(ReplaceUnderscores.class)
class PassengerDiscountPolicyTest {

    @Test
    void 성인_승객은_0프로_할인_받는다() {
        // given
        final Money 천원 = Money.from("1000");
        final PassengerDiscountType 승객_할인_타입 = PassengerDiscountType.findBy(ADULT);

        // when
        final Money 할인할_금액 = 승객_할인_타입.doDiscount(천원);

        // then
        assertThat(할인할_금액)
                .usingRecursiveComparison()
                .isEqualTo(Money.from("0"));
    }
}
