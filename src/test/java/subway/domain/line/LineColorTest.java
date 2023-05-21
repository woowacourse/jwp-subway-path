package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class LineColorTest {

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("노선의 색이 비어있으면 예외가 발생해야 한다.")
    void create_blank(String color) {
        // expect
        assertThatThrownBy(() -> new LineColor(color))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 색은 빈 값이 될 수 없습니다.");
    }

    @Test
    @DisplayName("노선의 색의 길이가 15글자를 초과하면 예외가 발생해야 한다.")
    void create_overThan15Characters() {
        // expect
        assertThatThrownBy(() -> new LineColor("123456789012345"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 색은 15글자를 초과할 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"green", "bg-red-0", "bg-red-123", "bg-red-1000"})
    @DisplayName("노선의 색이 색의 형식과 맞지 않으면 에외가 발생해야 한다.")
    void create_invalidFormat(String color) {
        // expect
        assertThatThrownBy(() -> new LineColor(color))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 색은 bg-(소문자로 된 색 단어)-(1~9로 시작하는 100 단위의 수) 여야 합니다.");
    }

    @Test
    @DisplayName("노선의 색이 정상적으로 생성되어야 한다.")
    void create_success() {
        // when
        LineColor lineColor = new LineColor("bg-red-100");

        // expect
        assertThat(lineColor.getColor())
                .isEqualTo("bg-red-100");
    }
}
