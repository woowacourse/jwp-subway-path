package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinesTest {

    @DisplayName("id에 해당하는 노선이 없으면 예외를 발생시킨다.")
    @Test
    void shouldThrowExceptionWhenIdNoExist() {
        final Lines lines = Lines.of(List.of(new Line(1L, "1호선", "파란색")));
        assertThatThrownBy(() -> lines.getLine(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @DisplayName("id에 해당하는 노선을 반환한다.")
    @Test
    void getLine() {
        final Lines lines = Lines.of(List.of(new Line(1L, "1호선", "파란색")));
        final Line line = lines.getLine(1L);
        assertAll(
                () -> assertThat(line.getId()).isEqualTo(1L),
                () -> assertThat(line.getName()).isEqualTo("1호선"),
                () -> assertThat(line.getColor()).isEqualTo("파란색")
        );
    }

    @DisplayName("하나의 노선도 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void shouldThrowExceptionWhenNoLine() {
        final Lines lines = Lines.of(Collections.emptyList());
        assertThatThrownBy(lines::getAllIds)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("노선이 하나도 존재하지 않습니다.");
    }

    @DisplayName("존재하는 모든 노선의 id를 반환한다.")
    @Test
    void getAllIds() {
        final Lines lines = Lines.of(List.of(new Line(1L, "1호선", "파란색"), new Line(2L, "2호선", "초록색")));
        assertThat(lines.getAllIds()).containsExactly(1L, 2L);
    }
}
