package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"5,BABY", "8,KID", "15,TEEN", "19,ADULT"})
    @DisplayName("나이에 맞는 정책을 찾는다.")
    void search(final int age, final AgePolicy expected) {
        final AgePolicy result = AgePolicy.search(age);

        assertThat(result).isEqualTo(expected);
    }

    @Nested
    @DisplayName("할인 금액을 계산할 때 ")
    class CalculateDiscountFare {

        @Test
        @DisplayName("BABY는 할인되지 않는다.")
        void baby() {
            final AgePolicy agePolicy = AgePolicy.BABY;

            final double result = agePolicy.calculateDiscountFare(1000);

            assertThat(result).isEqualTo(1000);
        }

        @Test
        @DisplayName("KID는 350원 공제 후, 50% 할인된 금액을 반환한다.")
        void kid() {
            final AgePolicy agePolicy = AgePolicy.KID;

            final double result = agePolicy.calculateDiscountFare(1350);

            assertThat(result).isEqualTo(500);
        }

        @Test
        @DisplayName("TEEN은 350원 공제 후, 20% 할인된 금액을 반환한다.")
        void teen() {
            final AgePolicy agePolicy = AgePolicy.TEEN;

            final double result = agePolicy.calculateDiscountFare(1350);

            assertThat(result).isEqualTo(800);
        }

        @Test
        @DisplayName("ADULT는 할인되지 않는다.")
        void adult() {
            final AgePolicy agePolicy = AgePolicy.ADULT;

            final double result = agePolicy.calculateDiscountFare(1000);

            assertThat(result).isEqualTo(1000);
        }
    }
}
