package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.vo.Name;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ColorTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 색상이_공백이거나_null일_경우_예외가_발생한다(final String nameValue) {
        assertThatThrownBy(() -> new Name(nameValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "01234567890"})
    void 색상의_길이가_1미만_10초과일_경우_예외가_발생한다(final String nameValue) {
        assertThatThrownBy(() -> new Name(nameValue))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
