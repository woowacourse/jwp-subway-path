package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.fare.AgeGroup.ADULT;
import static subway.domain.fare.AgeGroup.CHILD;
import static subway.domain.fare.AgeGroup.YOUTH;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AgeGroupTest {

    @Test
    void 성인의_경우_할인이_적용되지_않는다() {
        // given
        final int fare = 1250;

        // when
        final int result = ADULT.calculate(fare);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    void 청소년의_경우_350원을_할인한_금액에서_20퍼센트가_할인된다() {
        // given
        final int fare = 1250;

        // when
        final int result = YOUTH.calculate(fare);

        // then
        assertThat(result).isEqualTo(720);
    }

    @Test
    void 어린이의_경우_350원을_할인한_금액에서_50퍼센트가_할인된다() {
        // given
        final int fare = 1250;

        // when
        final int result = CHILD.calculate(fare);

        // then
        assertThat(result).isEqualTo(450);
    }
}
