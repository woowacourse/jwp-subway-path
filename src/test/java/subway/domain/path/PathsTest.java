package subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PathsTest {

    private final Station station = new Station("서면");
    private final Station station2 = new Station("해운대");
    private final Station station3 = new Station("광안리");
    private final Station station4 = new Station("전포");

    @DisplayName("생성 테스트")
    @Nested
    class Construct {
        @DisplayName("여러 경로들로 Paths를 생성할 수 있다")
        @Test
        void construct() {
            //given
            final Path path1 = new Path(station, station2, 3);
            final Path path2 = new Path(station2, station3, 3);
            final Path path3 = new Path(station3, station4, 3);

            //when, then
            assertDoesNotThrow(() -> new Paths(List.of(path1, path2, path3)));
        }

        @DisplayName("이어지지 않은 경로들로 Paths를 생성하면 예외가 발생한다")
        @Test
        void construct_fail() {
            //given
            final Path path1 = new Path(station, station2, 3);
            final Path path2 = new Path(station3, station4, 3);

            //when, then
            assertThatThrownBy(() -> new Paths(List.of(path1, path2)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("연결되지 않은 경로들입니다.");
        }

        @DisplayName("1:1 연결이 아닌 경로들로 Paths를 생성하면 예외가 발생한다")
        @Test
        void construct_fail2() {
            //given
            final Path path1 = new Path(station, station2, 3);
            final Path path2 = new Path(station, station3, 3);
            final Path path3 = new Path(station3, station4, 3);

            //when, then
            assertThatThrownBy(() -> new Paths(List.of(path1, path2, path3)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("연결되지 않은 경로들입니다.");
        }
    }

    @DisplayName("경로가 하나도 없을 때 경로를 추가할 수 있다")
    @Test
    void addPath() {
        //given
        Paths paths = new Paths();

        //when, then
        paths = paths.addPath(new Path(station, station2, 3));

        //then
        assertThat(paths.toList()).hasSize(1);
    }

    @DisplayName("다른 경로가 있을 때 이어져있지 않은 경로를 추가하면 예외가 발생한다")
    @Test
    void addPath_fail() {
        //given
        final Path path = new Path(station, station2, 3);
        final Paths paths = new Paths(List.of(path));

        //when, then
        final Path path2 = new Path(station3, station4, 3);
        assertThatThrownBy(() -> paths.addPath(path2))
                .hasMessage("기존의 역과 이어져야 합니다.");
    }

    @DisplayName("다른 경로가 있을 때 존재하는 두 역의 새 경로를 추가하면 예외가 발생한다")
    @Test
    void addPath_fail2() {
        //given
        final Path path = new Path(station, station2, 3);
        final Paths paths = new Paths(List.of(path));

        //when, then
        final Path path2 = new Path(station, station2, 5);
        assertThatThrownBy(() -> paths.addPath(path2))
                .hasMessage("이미 존재하는 경로입니다.");
    }

    @DisplayName("다른 경로가 있을 때 사이에 경로를 추가할 수 있다")
    @Test
    void addPath_between() {
        //given
        final Path path = new Path(station, station2, 3);
        Paths paths = new Paths(List.of(path));

        //when, then
        final Path path2 = new Path(station, station3, 1);
        paths = paths.addPath(path2);

        //then
        assertThat(paths.toList()).hasSize(2);
    }

    @DisplayName("경로를 제거할 수 있다")
    @Test
    void removePath() {
        //given
        final Path path = new Path(station, station2, 3);
        Paths paths = new Paths(List.of(path));

        final int before = paths.toList().size();

        //when
        paths = paths.removePath(path.getDown());
        final int after = paths.toList().size();

        //then
        assertAll(
                () -> assertThat(before).isOne(),
                () -> assertThat(after).isZero());
    }

    @DisplayName("순서대로 반환한다")
    @Test
    void getOrderedPaths() {
        //given
        final Path path1 = new Path(station, station2, 3);
        final Path path2 = new Path(station2, station3, 3);
        final Path path3 = new Path(station3, station4, 3);
        final Paths paths = new Paths(List.of(path2, path3, path1));

        //when
        final List<Path> orderedPaths = paths.getOrdered();

        //then
        assertThat(orderedPaths).containsExactly(path1, path2, path3);
    }

    @DisplayName("포함하는 모든 역을 가져올 수 있다")
    @Test
    void getStations() {
        //given
        final Path path1 = new Path(station, station2, 3);
        final Path path2 = new Path(station2, station3, 3);
        final Path path3 = new Path(station3, station4, 3);
        final Paths paths = new Paths(List.of(path2, path3, path1));

        //when
        final List<Station> stations = paths.getStations();

        //then
        assertThat(stations).hasSize(4);
    }
}
