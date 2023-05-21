package subway.domain.fare.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.AgeGroup;
import subway.domain.fare.FareInformation;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("AgeDiscountPolicy 은(는)")
class AgeDiscountPolicyTest {

    private final AgeDiscountPolicy discountPolicy = new AgeDiscountPolicy();

    @Nested
    class 금액을_계산한다 {

        @ParameterizedTest
        @CsvSource(value = {"1000:1000", "1500:1500", "2000:2000"}, delimiter = ':')
        void 성인의_경우(final int given, final int expected) {
            // given
            final FareInformation fareInformation = new FareInformation(10, null, AgeGroup.ADULT);

            // when
            final int actual = discountPolicy.calculate(given, fareInformation);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {"1350:800", "2350:1600"}, delimiter = ':')
        void 청소년의_경우(final int given, final int expected) {
            // given
            final FareInformation fareInformation = new FareInformation(10, null, AgeGroup.YOUTH);

            // when
            final int actual = discountPolicy.calculate(given, fareInformation);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {"1350:500", "2350:1000"}, delimiter = ':')
        void 어린이의_경우(int given, int expected) {
            // given
            final FareInformation fareInformation = new FareInformation(10, null, AgeGroup.CHILD);

            // when
            final int actual = discountPolicy.calculate(given, fareInformation);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {"1350:0", "2600:0"}, delimiter = ':')
        void 미취학아동의_경우(int given, int expected) {
            // given
            final FareInformation fareInformation = new FareInformation(10, null, AgeGroup.PRESCHOOLERS);

            // when
            final int actual = discountPolicy.calculate(given, fareInformation);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }
}
