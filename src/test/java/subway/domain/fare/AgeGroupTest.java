package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.fare.AgeGroup.ADULT;
import static subway.domain.fare.AgeGroup.BABY;
import static subway.domain.fare.AgeGroup.CHILD;
import static subway.domain.fare.AgeGroup.YOUTH;
import static subway.domain.fare.AgeGroup.from;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.exception.InvalidAgeException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AgeGroupTest {

    @Test
    void 올바른_나이가_아닌_경우_예외를_던진다() {
        assertThatThrownBy(() -> from(-1))
                .isInstanceOf(InvalidAgeException.class)
                .hasMessage("올바른 나이가 아닙니다.");
    }

    @CsvSource({
            "0, BABY",
            "5, BABY",
            "6, CHILD",
            "12, CHILD",
            "13, YOUTH",
            "18, YOUTH",
            "19, ADULT",
    })
    @ParameterizedTest(name = "{0}인 경우 {1}을 반환한다")
    void 올바른_나이인_경우_해당_나이에_맞는_그룹을_반환한다(final int age, final AgeGroup group) {
        assertThat(from(age)).isEqualTo(group);
    }

    @Test
    void 성인의_경우_할인이_적용되지_않는다() {
        // given
        final int fare = 1250;

        // when
        final int result = ADULT.calculateFare(fare);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    void 청소년의_경우_350원을_할인한_금액에서_20퍼센트가_할인된다() {
        // given
        final int fare = 1250;

        // when
        final int result = YOUTH.calculateFare(fare);

        // then
        assertThat(result).isEqualTo(720);
    }

    @Test
    void 어린이의_경우_350원을_할인한_금액에서_50퍼센트가_할인된다() {
        // given
        final int fare = 1250;

        // when
        final int result = CHILD.calculateFare(fare);

        // then
        assertThat(result).isEqualTo(450);
    }

    @Test
    void 유아의_경우_무료다() {
        // given
        final int fare = 1250;

        // when
        final int result = BABY.calculateFare(fare);

        // then
        assertThat(result).isZero();
    }
}
