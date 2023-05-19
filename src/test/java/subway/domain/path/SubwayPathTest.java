package subway.domain.path;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.edge.StationEdge;

import java.util.List;

class SubwayPathTest {

    @Test
    @DisplayName("이전 노선에 이어서 경로를 추가한다.")
    void add_path_in_same_line_test() {
        //given
        final SubwayPath subwayPath = new SubwayPath();
        subwayPath.add(1L, new StationEdge(1L, 2L, 3));

        // when
        subwayPath.add(1L, new StationEdge(2L, 3L, 4));

        // then
        final List<LinePath> linePaths = subwayPath.getLinePaths();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(linePaths).hasSize(1);
            softly.assertThat(linePaths.get(0).getStationIds()).containsExactly(1L, 2L, 3L);
        });
    }

    @Test
    @DisplayName("환승 노선에 이어서 경로를 추가한다.")
    void add_path_in_transfer_line_test() {
        //given
        final SubwayPath subwayPath = new SubwayPath();
        subwayPath.add(1L, new StationEdge(1L, 2L, 3));

        // when
        subwayPath.add(2L, new StationEdge(2L, 3L, 4));

        // then
        final List<LinePath> linePaths = subwayPath.getLinePaths();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(linePaths).hasSize(2);
            softly.assertThat(linePaths.get(0).getStationIds()).containsExactly(1L, 2L);
            softly.assertThat(linePaths.get(1).getStationIds()).containsExactly(2L, 3L);
        });
    }
}
