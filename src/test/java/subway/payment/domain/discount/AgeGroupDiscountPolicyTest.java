package subway.payment.domain.discount;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.path.domain.AgeGroup.ADULTS;
import static subway.path.domain.AgeGroup.KIDS;
import static subway.path.domain.AgeGroup.SENIOR;
import static subway.path.domain.AgeGroup.TEENAGERS;
import static subway.path.domain.AgeGroup.TODDLER;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("AgeGroupDiscountPolicy 은(는)")
class AgeGroupDiscountPolicyTest {

    private final AgeGroupDiscountPolicy policy = new AgeGroupDiscountPolicy();

    @Test
    void 성인은_할인같은거_없다() {
        // when
        final DiscountResult discount = policy.discount(1250);

        // then
        final Integer fee = discount.feeByDiscountInfo()
                .get(ADULTS.info());
        assertThat(fee).isEqualTo(1250);

    }

    @Test
    void 유아는_꽁짜다() {
        // when
        final DiscountResult discount = policy.discount(1250);

        // then
        final Integer fee = discount.feeByDiscountInfo()
                .get(TODDLER.info());
        assertThat(fee).isEqualTo(0);
    }

    @Test
    void 어린이는_일반_금액에서_350원을_제하고_50퍼를_할인한다() {
        // when
        final DiscountResult discount = policy.discount(1250);

        // then
        final Integer fee = discount.feeByDiscountInfo()
                .get(KIDS.info());
        assertThat(fee).isEqualTo(450);
    }

    @Test
    void 청소년은_일반_금액에서_350원을_제하고_20퍼를_할인한다() {
        // when
        final DiscountResult discount = policy.discount(1250);

        // then
        final Integer fee = discount.feeByDiscountInfo()
                .get(TEENAGERS.info());
        assertThat(fee).isEqualTo(720);
    }

    @Test
    void 노인은_무료이다() {
        // when
        final DiscountResult discount = policy.discount(1250);

        // then
        final Integer fee = discount.feeByDiscountInfo()
                .get(SENIOR.info());
        assertThat(fee).isEqualTo(0);
    }
}
