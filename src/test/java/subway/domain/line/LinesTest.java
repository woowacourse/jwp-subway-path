package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.edge.StationEdge;
import subway.exception.DuplicatedLineNameException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    @DisplayName("상행과 하행 역의 아이디로 노선아이디를 얻는다.")
    void get_line_id_by_station_ids_test() {
        // given
        final Line line1 = Line.of(1L, "line1", "green", Set.of(new StationEdge(1L, 2L, 4)));
        final Line line2 = Line.of(2L, "line2", "blue", Set.of(new StationEdge(2L, 3L, 4)));
        final Lines lines = new Lines();
        lines.add(List.of(line1, line2));

        // when
        final Long lineIdBySection = lines.getLineIdBySection(2L, 3L);

        // then
        assertThat(lineIdBySection).isEqualTo(2L);
    }

}
