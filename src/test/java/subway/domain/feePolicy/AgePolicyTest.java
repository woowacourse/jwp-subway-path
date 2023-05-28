package subway.domain.feePolicy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.feePolicy.AgePolicy.DEFAULT;
import static subway.domain.feePolicy.AgePolicy.KIDS;
import static subway.domain.feePolicy.AgePolicy.YOUTH;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgePolicyTest {

    @Test
    void 나이에_따라_정책이_달라진다() {
        assertAll(
                () -> assertThat(AgePolicy.from(0)).isEqualTo(DEFAULT),
                () -> assertThat(AgePolicy.from(6)).isEqualTo(KIDS),
                () -> assertThat(AgePolicy.from(12)).isEqualTo(KIDS),
                () -> assertThat(AgePolicy.from(13)).isEqualTo(YOUTH),
                () -> assertThat(AgePolicy.from(18)).isEqualTo(YOUTH),
                () -> assertThat(AgePolicy.from(19)).isEqualTo(DEFAULT)
        );
    }

    @ParameterizedTest
    @CsvSource({"6, 1250, 450", "12, 1250, 450", "13, 1250, 720", "18, 1250, 720", "5, 1250, 1250", "20, 1250, 1250"})
    void 나이에_따라_요금을_할인한다(final int age, final int fee, final int expect) {
        // given
        AgePolicy agePolicy = AgePolicy.from(age);

        // when
        int result = agePolicy.discount(fee);

        // then
        assertThat(result).isEqualTo(expect);
    }
}
