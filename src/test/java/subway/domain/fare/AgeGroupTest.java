package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("AgeGroup 은(는)")
class AgeGroupTest {

    @Nested
    class 나이를_입력받아 {

        @ParameterizedTest
        @ValueSource(ints = {20, Integer.MAX_VALUE})
        void 정상적으로_성인을_생성한다(int adultAge) {
            // when
            AgeGroup actual = AgeGroup.from(adultAge);

            // then
            assertThat(actual).isEqualTo(AgeGroup.ADULT);
        }

        @ParameterizedTest
        @ValueSource(ints = {13, 18})
        void 정상적으로_청소년을_생성한다(int youthAge) {
            // when
            AgeGroup actual = AgeGroup.from(youthAge);

            // then
            assertThat(actual).isEqualTo(AgeGroup.YOUTH);
        }

        @ParameterizedTest
        @ValueSource(ints = {6, 12})
        void 정상적으로_어린이를_생성한다(int childAge) {
            // when
            AgeGroup actual = AgeGroup.from(childAge);

            // then
            assertThat(actual).isEqualTo(AgeGroup.CHILD);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 5})
        void 정상적으로_미취학아동_생성한다(int preschoolersAge) {
            // when
            AgeGroup actual = AgeGroup.from(preschoolersAge);

            // then
            assertThat(actual).isEqualTo(AgeGroup.PRESCHOOLERS);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -10, -100})
        void 잘못된_나이면_예외(int wrongAge) {
            assertThatThrownBy(() -> AgeGroup.from(wrongAge))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 금액을_계산한다 {

        @ParameterizedTest
        @CsvSource(value = {"1000:1000", "1500:1500", "2000:2000"}, delimiter = ':')
        void 성인의_경우(int given, int expected) {
            // given
            AgeGroup adult = AgeGroup.ADULT;

            // when
            int actual = adult.calculate(given);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {"1350:800", "2350:1600"}, delimiter = ':')
        void 청소년의_경우(int given, int expected) {
            // given
            AgeGroup adult = AgeGroup.YOUTH;

            // when
            int actual = adult.calculate(given);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {"1350:500", "2350:1000"}, delimiter = ':')
        void 어린이의_경우(int given, int expected) {
            // given
            AgeGroup adult = AgeGroup.CHILD;

            // when
            int actual = adult.calculate(given);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {"1350:0", "2600:0"}, delimiter = ':')
        void 미취학아동의_경우(int given, int expected) {
            // given
            AgeGroup adult = AgeGroup.PRESCHOOLERS;

            // when
            int actual = adult.calculate(given);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }
}
