package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AgeDiscountPolicyTest {

    private AgeDiscountPolicy ageDiscountPolicy;

    @BeforeEach
    void setUp() {
        ageDiscountPolicy = new AgeDiscountPolicy();
    }

    @Test
    void 성인_요금_할인() {
        Fare fare = ageDiscountPolicy.calculate(null, 19, new Fare(1250));

        assertThat(fare).isEqualTo(new Fare(1250));
    }

    @Test
    void 청소년_요금_할인() {
        Fare fare = ageDiscountPolicy.calculate(null, 13, new Fare(1250));

        assertThat(fare).isEqualTo(new Fare(720));
    }

    @Test
    void 어린이_요금_할인() {
        Fare fare = ageDiscountPolicy.calculate(null, 6, new Fare(1250));

        assertThat(fare).isEqualTo(new Fare(450));
    }

    @Test
    void 영유아_요금_할인() {
        Fare fare = ageDiscountPolicy.calculate(null, 0, new Fare(1250));

        assertThat(fare).isEqualTo(new Fare(0));
    }

}
