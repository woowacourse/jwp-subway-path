package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineTest {

    @DisplayName("역을 가질 수 있다")
    @Test
    void createLine() {
        //given
        final Station station = new Station("서면역");
        final Path path = new Path(station, station, 4);
        final Paths paths = new Paths(List.of(path));

        //when, then
        assertDoesNotThrow(() -> new Line("1호선", "red", paths));
    }

    @DisplayName("거리는 양의 정수만 가능하다")
    @Test
    void positiveDistance() {
        //given
        final Station station = new Station("서면역");
        final Path path1 = new Path(station, station, 1);
        final Path path2 = new Path(station, station, 1000);
        final Paths paths = new Paths(List.of(path1, path2));

        //when, then
        assertDoesNotThrow(() -> new Line("1호선", "red", paths));
    }

    @DisplayName("거리가 0 이하이면 예외가 발생한다")
    @Test
    void notPositiveDistance() {
        //given
        final Station station = new Station("서면역");

        //when, then
        assertThatThrownBy(() -> new Path(station, station, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
