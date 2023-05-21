package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
}
