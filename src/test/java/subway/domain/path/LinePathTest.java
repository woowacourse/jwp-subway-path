package subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.edge.StationEdge;
import subway.domain.line.edge.StationEdges;
import subway.exception.IllegalStationEdgeStateException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LinePathTest {

    @Test
    @DisplayName("경로를 이어서 추가한다.")
    void add_path_test() {
        // given
        final LinePath linePath = new LinePath(1L, StationEdges.from(new StationEdge(1L, 2L, 3)));

        // when
        linePath.add(new StationEdge(2L, 3L, 4));

        // then
        assertThat(linePath.getStationIds()).containsExactly(1L, 2L, 3L);
    }

    @Test
    @DisplayName("연결되지 않는 경로를 추가할 수 없다.")
    void add_path_fail_test() {
        // given
        final LinePath linePath = new LinePath(1L, StationEdges.from(new StationEdge(1L, 2L, 3)));
        final StationEdge notConnectedEdge = new StationEdge(1L, 3L, 3);

        // when
        // then
        assertThatThrownBy(() -> linePath.add(notConnectedEdge))
                .isInstanceOf(IllegalStationEdgeStateException.class)
                .hasMessage("경로가 연결되지 않습니다.");
    }
}
