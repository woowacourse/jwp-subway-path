package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DiscountPolicyTest {

    @ParameterizedTest
    @CsvSource({"6, 1250, 450", "12, 1250, 450", "13, 1250, 720", "18, 1250, 720", "5, 1250, 1250", "20, 1250, 1250"})
    void 나이에_따라_요금을_할인한다(final int age, final int fee, final int expect) {
        int result = DiscountPolicy.discount(fee, age);

        assertThat(result).isEqualTo(expect);
    }
}
