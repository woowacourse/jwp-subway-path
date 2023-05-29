package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.policy.discount.AgeDiscountPolicy;

@DisplayName("AgeDiscountPolicy 기능 테스트")
public class AgeDiscountPolicyTest {

    @Test
    @DisplayName("미취학 아동은 0원으로 할인된다.")
    void babyDiscountPolicyTest() {
        // given
        AgeDiscountPolicy policy = AgeDiscountPolicy.from(5);

        // when
        int finalFare = policy.calculate(1250);

        // then
        assertThat(finalFare).isEqualTo(0);
    }

    @Test
    @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50%할인한다.")
    void childDiscountPolicyTest() {
        // given
        AgeDiscountPolicy policy = AgeDiscountPolicy.from(7);

        // when
        int finalFare = policy.calculate(1250);

        // then
        assertThat(finalFare).isEqualTo(450);
    }

    @Test
    @DisplayName("청소년은 운임에서 350원을 공제한 금액의 20%할인한다.")
    void teenDiscountPolicyTest() {
        // given
        AgeDiscountPolicy policy = AgeDiscountPolicy.from(17);

        // when
        int finalFare = policy.calculate(1250);

        // then
        assertThat(finalFare).isEqualTo(720);
    }

    @Test
    @DisplayName("성인은 기본 요금을 부과한다.")
    void adultDiscountPolicyTest() {
        // given
        AgeDiscountPolicy policy = AgeDiscountPolicy.from(20);

        // when
        int finalFare = policy.calculate(1250);

        // then
        assertThat(finalFare).isEqualTo(1250);
    }
}
