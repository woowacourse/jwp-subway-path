package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinesTest {

    @Test
    @DisplayName("생성되는 노선들 중 중복되는 노선 이름이 있으면 예외가 발생한다.")
    void generate_validateDuplicateLineName() {
        // given
        List<Line> lines = List.of(new Line(1L, "2호선"), new Line(2L, "2호선"));

        // when, then
        assertThatThrownBy(() -> new Lines(lines))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 이름은 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("노선 이름에 해당하는 노선이 있으면 반환한다.")
    void findByLineName_exist() {
        // given
        String lineName = "1호선";
        Line line1 = new Line(1L, lineName);
        Line line2 = new Line(2L, "2호선");
        Lines lines = new Lines(List.of(line1, line2));

        // when
        Optional<Line> findLine = lines.findByLineName(lineName);

        // then
        assertThat(findLine.get()).usingRecursiveComparison().isEqualTo(line1);
    }

    @Test
    @DisplayName("노선 이름에 해당하는 노선이 없으면 빈 Optional을 반환한다.")
    void findByLineName_not_exist_OptionalEmpty() {
        // given
        String lineName = "-100호선";
        Line line1 = new Line(1L, "1호선");
        Line line2 = new Line(2L, "2호선");
        Lines lines = new Lines(List.of(line1, line2));

        // when
        Optional<Line> findLine = lines.findByLineName(lineName);

        // then
        assertThat(findLine.isEmpty()).isTrue();
    }
}
