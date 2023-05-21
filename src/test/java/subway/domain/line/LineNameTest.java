package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

class LineNameTest {

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("노선의 이름이 비어있으면 예외가 발생해야 한다.")
    void create_blank(String name) {
        // expect
        assertThatThrownBy(() -> new LineName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 이름은 빈 값이 될 수 없습니다.");
    }

    @Test
    @DisplayName("노선의 이름의 길이가 15글자를 초과하면 예외가 발생해야 한다.")
    void create_overThan15Characters() {
        // expect
        assertThatThrownBy(() -> new LineName("123456789012345"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 이름은 15글자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("노선의 이름이 정상적으로 생성되어야 한다.")
    void create_success() {
        // when
        LineName lineName = new LineName("2호선");

        // then
        assertThat(lineName.getName())
                .isEqualTo("2호선");
    }
}
