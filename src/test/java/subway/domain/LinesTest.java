package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.line.Lines;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Lines는 ")
class LinesTest {


    @Test
    @DisplayName("노선들을 갖는다.")
    void linesCreateTest() {
        // given
        Line line1 = Line.of("1호선", "남색", 1000);
        Line line2 = Line.of("2호선", "초록색", 1000);

        // then
        assertDoesNotThrow(() -> Lines.from(new ArrayList<>(List.of(line1, line2))));
    }

    @Test
    @DisplayName("중복된 이름을 가지는 노선을 생성할 수 없다.")
    void duplicatedLineNameExceptionTest() {
        // given
        Line line1 = Line.of("1호선", "남색", 1000);
        Line line2 = Line.of("2호선", "초록색", 1000);
        Line line3 = Line.of("2호선", "빨간색", 1000);
        Lines lines = Lines.from(new ArrayList<>(List.of(line1, line2)));

        // then
        assertThatThrownBy(() -> lines.add(line3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 중복되는 이름으로 노선을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("중복된 색상을 가지는 노선을 생성할 수 없다.")
    void duplicatedLineColorExceptionTest() {
        // given
        Line line1 = Line.of("1호선", "남색", 1000);
        Line line2 = Line.of("2호선", "초록색", 1000);
        Line line3 = Line.of("3호선", "초록색", 1000);
        Lines lines = Lines.from(new ArrayList<>(List.of(line1, line2)));

        // then
        assertThatThrownBy(() -> lines.add(line3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 중복되는 색상으로 노선을 생성할 수 없습니다.");
    }
}
