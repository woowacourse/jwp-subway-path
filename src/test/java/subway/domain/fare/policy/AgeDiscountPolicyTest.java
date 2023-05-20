package subway.domain.fare.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.fare.AgeGroup;
import subway.domain.fare.FareInformation;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("AgeDiscountPolicy 은(는)")
class AgeDiscountPolicyTest {

    private final AgeDiscountPolicy discountPolicy = new AgeDiscountPolicy();

    @Test
    void 나이에_따라_요금을_계산한다() {
        // given
        FareInformation fareInformation = new FareInformation(10, null, AgeGroup.CHILD);
        int fare = 1350;

        // when
        int actual = discountPolicy.calculate(fare, fareInformation);

        // then
        assertThat(actual).isEqualTo(500);
    }
}
