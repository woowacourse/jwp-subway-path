package subway.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import subway.exception.ColorNotBlankException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ColorTest {

    @ParameterizedTest
    @EmptySource
    @DisplayName("색상이 공백이면 예외를 발생시킨다.")
    void throws_exception_when_color_is_blank(final String input) {
        // when & then
        assertThatThrownBy(() -> new Color(input))
                .isInstanceOf(ColorNotBlankException.class);
    }

    @Test
    @DisplayName("색상을 생성한다.")
    void create_color_success() {
        // given
        String givenColor = "red";

        // when
        Color color = new Color(givenColor);

        // then
        assertThat(color.getColor()).isEqualTo(givenColor);
    }
}
