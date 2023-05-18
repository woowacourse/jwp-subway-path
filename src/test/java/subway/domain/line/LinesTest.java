package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.edge.StationEdge;
import subway.exception.DuplicatedLineNameException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LinesTest {

    @Test
    @DisplayName("이미 생성된 이름으로 노선을 생성할 수 없다.")
    void add_line_with_already_created_name_test() {
        // given
        final Line line1 = Line.of(1L, "line1", "green", Set.of(new StationEdge(1L, 2L, 4)));
        final Line line2 = Line.of(2L, "line1", "green", Set.of(new StationEdge(1L, 2L, 4)));
        final Lines lines = new Lines();
        lines.add(line1);

        // when
        // then
        assertThatThrownBy(() -> lines.add(line2))
                .isInstanceOf(DuplicatedLineNameException.class);
    }

}
