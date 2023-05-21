package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.IllegalAgeException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("AgeGroup 은(는)")
class AgeGroupTest {

    @Nested
    class 나이를_입력받아 {

        @ParameterizedTest
        @ValueSource(ints = {20, Integer.MAX_VALUE})
        void 정상적으로_성인을_생성한다(final int adultAge) {
            // when
            final AgeGroup actual = AgeGroup.from(adultAge);

            // then
            assertThat(actual).isEqualTo(AgeGroup.ADULT);
        }

        @ParameterizedTest
        @ValueSource(ints = {13, 18})
        void 정상적으로_청소년을_생성한다(final int youthAge) {
            // when
            final AgeGroup actual = AgeGroup.from(youthAge);

            // then
            assertThat(actual).isEqualTo(AgeGroup.YOUTH);
        }

        @ParameterizedTest
        @ValueSource(ints = {6, 12})
        void 정상적으로_어린이를_생성한다(final int childAge) {
            // when
            final AgeGroup actual = AgeGroup.from(childAge);

            // then
            assertThat(actual).isEqualTo(AgeGroup.CHILD);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 5})
        void 정상적으로_미취학아동_생성한다(final int preschoolersAge) {
            // when
            final AgeGroup actual = AgeGroup.from(preschoolersAge);

            // then
            assertThat(actual).isEqualTo(AgeGroup.PRESCHOOLERS);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -10, -100})
        void 잘못된_나이면_예외(final int wrongAge) {
            assertThatThrownBy(() -> AgeGroup.from(wrongAge))
                    .isInstanceOf(IllegalAgeException.class);
        }
    }
}
