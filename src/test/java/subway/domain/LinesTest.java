package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinesTest {

    @Test
    @DisplayName("노선 이름이 중복되면 예외가 발생한다.")
    void add_fail_test() {
        // given
        final Line 신분당선 = new Line("신분당선", "bg-red-600");
        final Line 구신분당선 = new Line("구신분당선", "bg-red-600");
        final Lines lines = new Lines(List.of(신분당선, 구신분당선));

        // expect
        final Line 중복된_신분당선 = new Line("신분당선", "bg-red-500");
        assertThatThrownBy(() -> lines.add(중복된_신분당선))
            .isInstanceOf(IllegalArgumentException.class)
            .extracting("message")
            .isEqualTo("노선 이름은 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("노선을 추가할 수 있다.")
    void add_success_test() {
        // given
        final Line 구신분당선 = new Line("구신분당선", "bg-red-600");
        final Lines lines = new Lines(new ArrayList<>());

        // when
        lines.add(구신분당선);

        // then
        final List<Line> expectedLines = lines.getLines();
        assertThat(expectedLines.get(0))
            .extracting("name")
            .isEqualTo("구신분당선");
    }
}
