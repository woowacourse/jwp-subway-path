package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.path.Path;
import subway.domain.path.Paths;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineTest {

    @DisplayName("역을 가질 수 있다")
    @Test
    void createLine() {
        //given
        final Station station = new Station("서면역");
        final Station station2 = new Station("부산역");
        final Path path = new Path(station, station2, 4);
        final Paths paths = new Paths(List.of(path));

        //when, then
        assertDoesNotThrow(() -> new Line("1호선", "red", paths));
    }

    @DisplayName("역을 노선에 추가할 수 있다")
    @Test
    void addStation() {
        //given
        final Station station = new Station("서면역");
        final Station station2 = new Station("부산역");
        final Path path = new Path(station, station2, 1);
        Line line = new Line("1호선", "red");

        //when
        line = line.addPath(path);

        //then
        assertThat(line.getPathsSize()).isOne();
    }
}
